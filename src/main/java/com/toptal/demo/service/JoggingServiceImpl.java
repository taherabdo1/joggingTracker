package com.toptal.demo.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.controllers.filtter.CriteriaParser;
import com.toptal.demo.controllers.filtter.JogSpecification;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.JoggingRequestDTO;
import com.toptal.demo.dto.UpdateJogDto;
import com.toptal.demo.entities.Jogging;
import com.toptal.demo.entities.Location;
import com.toptal.demo.entities.User;
import com.toptal.demo.repositories.JoggingRepository;
import com.toptal.demo.repositories.LocationRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.service.weather.WeatherService;

@Service
public class JoggingServiceImpl implements JoggingService {

    @Autowired
    WeatherService weatherService;

    @Autowired
    JoggingRepository joggingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ModelMapper modelMapper;

    final DecimalFormat df = new DecimalFormat("####.##");

    @Override
    public JoggingReponseDto addNewJogging(final JoggingRequestDTO joggingRequestDTO, final String userEmail) throws ToptalException {
        final User user = userRepository.findOneByEmail(userEmail).orElse(null);

        final Jogging jogging = modelMapper.map(joggingRequestDTO, Jogging.class);

        // add the user to the new jogging
        jogging.setUser(user);

        Location location = locationRepository.findByLocationName(jogging.getLocation().getLocationName());

        // if it is a new location for the system add it
        if (null == location) {
            location = locationRepository.save(jogging.getLocation());
        }
        jogging.setLocation(location);

        weatherService.getWeather(jogging);

        // get all joggings to check for overlaps
        final Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);

        final List<Jogging> all = joggingRepository.findByUserEmail(user.getEmail(), pageRequest);
        // all = getPage(pageRequest, all);
        final Calendar calendar = Calendar.getInstance();
        for (final Jogging run : all) {
            calendar.setTimeInMillis(run.getDate().getTime());
            calendar.add(Calendar.MINUTE, run.getPeriodInMinutes());
            if (run.getDate().before(jogging.getDate()) && calendar.getTime().after(jogging.getDate())
                    || run.getDate().getTime() == jogging.getDate().getTime()) {
                throw ToptalError.JOGGING_OVERLAPING.buildException();
            }
        }
        final Jogging result = joggingRepository.save(jogging);
        final JoggingReponseDto response = modelMapper.map(result, JoggingReponseDto.class);
        response.setSpeed(Double.parseDouble(df.format(result.getDistance() / 1000.000 / (result.getPeriodInMinutes() / 60.000))));

        return response;
    }

    @Override
    public JoggingReponseDto getOne(final Long id) throws ToptalException {
        final Jogging selected = joggingRepository.findOne(id);
        if (selected == null) {
            throw ToptalError.JOGGING_NOT_FOUND.buildException();
        }
        final JoggingReponseDto response = modelMapper.map(selected, JoggingReponseDto.class);
        response.setSpeed(Double.parseDouble(df.format(selected.getDistance() / 1000.000 / (selected.getPeriodInMinutes() / 60.000))));

        return response;
    }

    @Override
    public JoggingReponseDto update(final UpdateJogDto updateJogDto, final String email) throws ToptalException {
        final Jogging selected = joggingRepository.findOne(updateJogDto.getId());
        if (selected == null) {
            throw ToptalError.JOGGING_NOT_FOUND.buildException();
        }
        final Jogging jogging = modelMapper.map(updateJogDto, Jogging.class);
        final User user = userRepository.findOne(selected.getUser().getId());
        final User loggedInUser = userRepository.findOneByEmail(email).get();// get the current logged in user
        // if the user is trying to update jog he is not own
        if (loggedInUser.getId() != user.getId()) {
            throw ToptalError.UNAUTHRIZED_USER_ERROR.buildException();
        }
        if (jogging.getLocation() != null) {
            Location location = locationRepository.findByLocationName(jogging.getLocation().getLocationName());
            // new location
            if (location == null) {
                location = locationRepository.save(jogging.getLocation());
            }
            jogging.setLocation(location);
        } else {
            jogging.setLocation(selected.getLocation());
        }
        jogging.setUser(user);

        // check for distance
        if (jogging.getDistance() == null) {
            jogging.setDistance(selected.getDistance());
        }
        // check for date
        if (jogging.getDate() == null) {
            jogging.setDate(selected.getDate());
        }
        // check for period
        if (jogging.getPeriodInMinutes() == null) {
            jogging.setPeriodInMinutes(selected.getPeriodInMinutes());
        }
        weatherService.getWeather(jogging);

        final Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);

        final List<Jogging> all = joggingRepository.findByUserEmail(user.getEmail(), pageRequest);

        final Calendar calendar = Calendar.getInstance();
        for (final Jogging run : all) {
            calendar.setTime(run.getDate());
            calendar.add(Calendar.MINUTE, run.getPeriodInMinutes());
            // skip the one we are updating now
            if (run.getId().equals(jogging.getId())) {
                continue;
            }
            if (run.getDate().before(jogging.getDate()) && calendar.getTime().after(jogging.getDate())
                    || run.getDate().getTime() == jogging.getDate().getTime()) {
                throw ToptalError.JOGGING_OVERLAPING.buildException();
            }
        }
        final Jogging result = joggingRepository.save(jogging);
        final JoggingReponseDto response = modelMapper.map(result, JoggingReponseDto.class);

        response.setSpeed(Double.parseDouble(df.format(result.getDistance() / 1000.000 / (result.getPeriodInMinutes() / 60.000))));

        return response;

    }

    @Override
    public List<JoggingReponseDto> getAllForAuser(final String userEmail, final int pageNumber, final int pageSize, final String filterBy)
        throws ToptalException {
        if (pageSize <= 0) {

            throw ToptalError.JOGGING_VALIDATION_ERROR_PAGE_SIZE.buildException();
        }
        if (pageNumber < 0) {
            throw ToptalError.JOGGING_VALIDATION_ERROR_PAGE_NUMBER.buildException();
        }

        final User user = userRepository.findOneByEmail(userEmail).get();
        final Pageable pageRequest = createPageRequest(pageSize, pageNumber);
        List<Jogging> joggingsList = null;

        if (filterBy != "" && filterBy != null) {
            try {
                final List<Object> filterObjects = CriteriaParser.parse(filterBy);
                final Specification<Jogging> userSpecification = new Specification<Jogging>() {
                    
                    @Override
                    public Predicate toPredicate(final Root<Jogging> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
                        return cb.equal(root.get("user"), user);
                    }
                };
                final Specification<Jogging> all = JogSpecification.getJogSpecification(filterObjects);

                joggingsList = Lists.newArrayList(
                        joggingRepository.findAll(Specifications.where(all).and(userSpecification), pageRequest).getContent());
            } catch (final Exception e) {
                throw ToptalError.INCORRECT_FILTER_CRITERIA.buildException();
            }

        } else {// no filter criteria needed
            joggingsList = Lists.newArrayList(joggingRepository.findAll(pageRequest).getContent());
        }

        // joggingsList = getPage(pageRequest, joggingsList);
        final List<JoggingReponseDto> response = new ArrayList<>();
        for (final Jogging jogging : joggingsList) {
            final JoggingReponseDto joggingReponseDto = modelMapper.map(jogging, JoggingReponseDto.class);

            joggingReponseDto.setSpeed(Double.parseDouble(df.format(jogging.getDistance() / 1000.000 / (jogging.getPeriodInMinutes() / 60.000))));

            response.add(joggingReponseDto);
        }
        return response;
    }

    @Override
    public List<JoggingReponseDto> getAll(final int pageNumber, final int pageSize, final String filterBy) throws ToptalException {
        if (pageSize <= 0) {
            throw ToptalError.JOGGING_VALIDATION_ERROR_PAGE_SIZE.buildException();
        }
        if (pageNumber < 0) {
            throw ToptalError.JOGGING_VALIDATION_ERROR_PAGE_NUMBER.buildException();
        }
        final Pageable pageRequest = createPageRequest(pageSize, pageNumber);
        final List<JoggingReponseDto> responseData = new ArrayList<>();
        List<Jogging> joggingsList = null;
        if (filterBy != "" && filterBy != null) {
            try {
                final List<Object> filterObjects = CriteriaParser.parse(filterBy);
                joggingsList = Lists.newArrayList(joggingRepository.findAll(JogSpecification.getJogSpecification(filterObjects), pageRequest).getContent());
            } catch (final Exception e) {
                throw ToptalError.INCORRECT_FILTER_CRITERIA.buildException();
            }

        } else {// no filter criteria needed
            joggingsList = Lists.newArrayList(joggingRepository.findAll(pageRequest).getContent());
        }

        // joggingsList = getPage(pageRequest, joggingsList);
        for (final Jogging jogging : joggingsList) {

            final JoggingReponseDto joggingReponseDto = modelMapper.map(jogging, JoggingReponseDto.class);

            joggingReponseDto.setSpeed(Double.parseDouble(df.format(jogging.getDistance() / 1000.000 / (jogging.getPeriodInMinutes() / 60.000))));
            responseData.add(joggingReponseDto);
        }
        return responseData;
    }

    @Override
    public boolean deleteJogging(final Long id) throws ToptalException {
        final Jogging selected = joggingRepository.findOne(id);
        if (selected == null) {
            throw ToptalError.JOGGING_NOT_FOUND.buildException();
        }
        joggingRepository.delete(selected);
        return true;
    }

    private Pageable createPageRequest(final int pageSize, final int pageNumber) {
        return new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "id");
    }

}
