package com.toptal.demo.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.controllers.filtter.CriteriaParser;
import com.toptal.demo.controllers.filtter.JogSpecification;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.JoggingRequestDTO;
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
        return response;
    }

    @Override
    public JoggingReponseDto getOne(final Long id) throws ToptalException {
        final Jogging selected = joggingRepository.findOne(id);
        if (selected == null) {
            throw ToptalError.JOGGING_NOT_FOUND.buildException();
        }
        final JoggingReponseDto response = modelMapper.map(selected, JoggingReponseDto.class);
        return response;
    }

    @Override
    public JoggingReponseDto update(final JoggingRequestDTO joggingRequestDTO) throws ToptalException {
        final Jogging selected = joggingRepository.findOne(joggingRequestDTO.getId());
        if (selected == null) {
            throw ToptalError.JOGGING_NOT_FOUND.buildException();
        }
        final Jogging jogging = modelMapper.map(joggingRequestDTO, Jogging.class);
        final User user = userRepository.findOne(selected.getUser().getId());
        Location location = locationRepository.findByLocationName(jogging.getLocation().getLocationName());
        // new location
        if (location == null) {
            location = locationRepository.save(jogging.getLocation());
        }
        jogging.setUser(user);
        jogging.setLocation(location);

        weatherService.getWeather(jogging);

        final Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);

        final List<Jogging> all = joggingRepository.findByUserEmail(user.getEmail(), pageRequest);

        final Calendar calendar = Calendar.getInstance();
        for (final Jogging run : all) {
            calendar.setTimeInMillis(run.getDate().getTime());
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
        final List<Object> conditions = CriteriaParser.parse(filterBy);
        final JogSpecification jogSpecification = new JogSpecification();
        jogSpecification.getJogSpecification(conditions);
        final Pageable pageRequest = createPageRequest(pageSize, pageNumber);
        final List<Jogging> joggingsList = joggingRepository.findByUserEmail(userEmail, pageRequest);
        // joggingsList = getPage(pageRequest, joggingsList);
        final List<JoggingReponseDto> response = new ArrayList<>();
        for (final Jogging jogging : joggingsList) {
            response.add(modelMapper.map(jogging, JoggingReponseDto.class));
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

        List<Jogging> joggingsList = Lists.newArrayList(joggingRepository.findAll(pageRequest));
        joggingsList = getPage(pageRequest, joggingsList);
        for (final Jogging jogging : joggingsList) {
            responseData.add(modelMapper.map(jogging, JoggingReponseDto.class));
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

    private List<Jogging> getPage(final Pageable pageable, final List<Jogging> jogs) {
        final int lowerLimit = 0;
        int upperLimit = 0;
        if ((pageable.getPageNumber() + 1) * pageable.getPageSize() > jogs.size()) {
            upperLimit = jogs.size();
        }
        if (pageable.getPageNumber() * pageable.getPageSize() > jogs.size()) {
            return new ArrayList();
        }
        List<Jogging> returnJogs = null;
        returnJogs = jogs.subList(lowerLimit, upperLimit);
        return returnJogs;
    }

}
