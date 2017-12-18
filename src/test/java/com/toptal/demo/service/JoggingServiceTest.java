package com.toptal.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.JoggingRequestDTO;
import com.toptal.demo.dto.LocationDto;
import com.toptal.demo.dto.UpdateJogDto;
import com.toptal.demo.entities.Jogging;
import com.toptal.demo.entities.Location;
import com.toptal.demo.entities.User;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.repositories.JoggingRepository;
import com.toptal.demo.repositories.LocationRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.service.weather.WeatherService;

@RunWith(MockitoJUnitRunner.class)
public class JoggingServiceTest {

    @InjectMocks
    JoggingServiceImpl joggingService;

    @Mock
    WeatherService weatherService;

    @Mock
    JoggingRepository joggingRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    LocationRepository locationRepository;

    @Mock
    ModelMapper modelMapper;


    final String USER_EMAIL = "test@demo.com";

    final User user = new User(1L, "test@demo.com", "password", 25, "user full name", Role.ROLE_USER, false, false, "cairo", null, null, null);
    
    final LocationDto locationDto = new LocationDto(123.2f, 123.3f, "cairo");

    final Location location = new Location(1L, 123.2f, 123.3f, "cairo", null);

    final JoggingRequestDTO joggingRequestDTO = new JoggingRequestDTO(new Date(), 45, 4000, locationDto);

    final JoggingReponseDto joggingReponseDto = new JoggingReponseDto(1L, new Date(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", locationDto, 5.5);

    final JoggingReponseDto updatedJoggingReponseDto = new JoggingReponseDto(1L, new Date(), 80, 8000, "WEATHER desctiption", "temprature", "1.2", locationDto,
            5.5);

    final Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);

    Calendar calendar = Calendar.getInstance();

    final Jogging jogging = new Jogging(null, calendar, 45, 4000, "WEATHER desctiption", "temprature", "1.2", location, user);

    final Jogging savedJogging = new Jogging(1L, calendar, 45, 4000, "WEATHER desctiption", "temprature", "1.2", location, user);

    final Jogging updateJogging = new Jogging(1L, calendar, 90, 8000, "WEATHER desctiption", "temprature", "1.2", location, user);

    final UpdateJogDto updateJogDto = new UpdateJogDto(1l, new Date(), 50, 1000, locationDto);

    @Rule
    public ExpectedException thrown = none();

    @Before
    public void init() {
        calendar.setTime(new Date());
    }
    @Test
    public void testaddNewJogging() throws ToptalException{
        doReturn(Optional.of(user)).when(userRepository).findOneByEmail(USER_EMAIL);
        doReturn(jogging).when(modelMapper).map(joggingRequestDTO, Jogging.class);
        doReturn(location).when(locationRepository).findByLocationName(jogging.getLocation().getLocationName());
        doReturn(new ArrayList<Jogging>()).when(joggingRepository).findByUserEmail(USER_EMAIL, pageRequest);
        doReturn(savedJogging).when(joggingRepository).save(jogging);
        doReturn(joggingReponseDto).when(modelMapper).map(savedJogging, JoggingReponseDto.class);

        final JoggingReponseDto savedoggingResponse = joggingService.addNewJogging(joggingRequestDTO, USER_EMAIL);
        assertNotNull(savedoggingResponse);
        assertEquals(savedoggingResponse.getDistance(), joggingRequestDTO.getDistance());

    }

    @Test
    public void testaddNewJoggingWithOverLappingError() throws ToptalException {
        final java.util.List<Jogging> jogs = new ArrayList<Jogging>();
        jogs.add(jogging);
        thrown.expectMessage(ToptalError.JOGGING_OVERLAPING.getDescription());

        doReturn(Optional.of(user)).when(userRepository).findOneByEmail(USER_EMAIL);
        doReturn(jogging).when(modelMapper).map(joggingRequestDTO, Jogging.class);
        doReturn(location).when(locationRepository).findByLocationName(jogging.getLocation().getLocationName());
        doReturn(jogs).when(joggingRepository).findByUserEmail(USER_EMAIL, pageRequest);
        doReturn(savedJogging).when(joggingRepository).save(jogging);
        doReturn(joggingReponseDto).when(modelMapper).map(savedJogging, JoggingReponseDto.class);

        joggingService.addNewJogging(joggingRequestDTO, USER_EMAIL);

    }

    @Test
    public void testGetOne() throws ToptalException {
        doReturn(jogging).when(joggingRepository).findOne(1L);
        doReturn(joggingReponseDto).when(modelMapper).map(jogging, JoggingReponseDto.class);

        final JoggingReponseDto response = joggingService.getOne(1L);
        assertNotNull(response);
        assertEquals(jogging.getLocation().getLocationName(), response.getLocation().getLocationName());
    }

    @Test
    public void testGetOneWithJogNotFoundException() throws ToptalException {
        doReturn(null).when(joggingRepository).findOne(1L);
        thrown.expectMessage(ToptalError.JOGGING_NOT_FOUND.getDescription());
        joggingService.getOne(1L);
    }

    @Test
    public void testaddUpdate() throws ToptalException {
        doReturn(savedJogging).when(joggingRepository).findOne(1L);
        doReturn(updateJogging).when(modelMapper).map(updateJogDto, Jogging.class);
        doReturn(user).when(userRepository).findOne(savedJogging.getUser().getId());
        doReturn(new ArrayList<Jogging>()).when(joggingRepository).findByUserEmail(USER_EMAIL, pageRequest);
        doReturn(updateJogging).when(joggingRepository).save(updateJogging);
        doReturn(updatedJoggingReponseDto).when(modelMapper).map(updateJogging, JoggingReponseDto.class);

        final JoggingReponseDto updateJoggingResponse = joggingService.update(updateJogDto);
        assertNotNull(updateJoggingResponse);
        assertNotEquals((Integer) updateJoggingResponse.getDistance(), savedJogging.getDistance());

    }

    @Test
    public void testUpdateNotFoundException() throws ToptalException {
        doReturn(null).when(joggingRepository).findOne(updateJogDto.getId());
        thrown.expectMessage(ToptalError.JOGGING_NOT_FOUND.getDescription());
        joggingService.update(updateJogDto);
    }

    @Test
    public void testUpdateWithOverlappingException() throws ToptalException {
        final java.util.List<Jogging> jogs = new ArrayList<Jogging>();

        jogs.add(new Jogging(2L, calendar, 90, 8000, "WEATHER desctiption", "temprature", "1.2", location, user));
        thrown.expectMessage(ToptalError.JOGGING_OVERLAPING.getDescription());

        doReturn(savedJogging).when(joggingRepository).findOne(1L);
        doReturn(updateJogging).when(modelMapper).map(updateJogDto, Jogging.class);
        doReturn(user).when(userRepository).findOne(savedJogging.getUser().getId());
        doReturn(jogs).when(joggingRepository).findByUserEmail(USER_EMAIL, pageRequest);
        doReturn(updateJogging).when(joggingRepository).save(updateJogging);
        doReturn(updatedJoggingReponseDto).when(modelMapper).map(updateJogging, JoggingReponseDto.class);

        joggingService.update(updateJogDto);

    }

    @Test
    public void testgetAllForAuser() throws ToptalException {
        doReturn(Optional.of(user)).when(userRepository).findOneByEmail(USER_EMAIL);
        doReturn(buildJogsList()).when(joggingRepository).findAll(createPageRequest(10, 0));
        doReturn(updatedJoggingReponseDto).when(modelMapper)
                .map(new Jogging(1L, calendar, 30, 8000, "WEATHER desctiption", "temprature", "1.2", location, user), JoggingReponseDto.class);

        final List<JoggingReponseDto> response = joggingService.getAllForAuser(USER_EMAIL, 0, 10, "");
        assertNotNull(response);
        assertEquals(response.size(), 1);

    }

    @Test
    public void testgetAllForAuserWithFilterStringNotEmpty() throws ToptalException {

        doReturn(Optional.of(user)).when(userRepository).findOneByEmail(USER_EMAIL);
        doReturn(buildJogsList()).when(joggingRepository).findAll(any(Specification.class), any(Pageable.class));
        doReturn(updatedJoggingReponseDto).when(modelMapper)
                .map(new Jogging(1L, calendar, 30, 8000, "WEATHER desctiption", "temprature", "1.2", location, user), JoggingReponseDto.class);

        final List<JoggingReponseDto> response = joggingService.getAllForAuser(USER_EMAIL, 0, 10, "(id eq 1)");
        assertNotNull(response);
        assertEquals(response.size(), 1);

    }

    @Test
    public void testgetAllForAuserWithFilterStringNotEmptyWithIncorrectFilterCriteria() throws ToptalException {
        thrown.expectMessage(ToptalError.INCORRECT_FILTER_CRITERIA.getDescription());

        doReturn(Optional.of(user)).when(userRepository).findOneByEmail(USER_EMAIL);
        doReturn(buildJogsList()).when(joggingRepository).findAll(any(Specification.class), any(Pageable.class));
        doReturn(updatedJoggingReponseDto).when(modelMapper)
                .map(new Jogging(1L, calendar, 30, 8000, "WEATHER desctiption", "temprature", "1.2", location, user), JoggingReponseDto.class);

        final List<JoggingReponseDto> response = joggingService.getAllForAuser(USER_EMAIL, 0, 10, "(((id eq 1))");
        assertNotNull(response);
        assertEquals(response.size(), 1);
    }

    @Test
    public void testgetAll() throws ToptalException {
        doReturn(buildJogsList()).when(joggingRepository).findAll(createPageRequest(10, 0));
        doReturn(updatedJoggingReponseDto).when(modelMapper)
                .map(new Jogging(1L, calendar, 30, 8000, "WEATHER desctiption", "temprature", "1.2", location, user), JoggingReponseDto.class);

        final List<JoggingReponseDto> response = joggingService.getAll(0, 10, "");
        assertNotNull(response);
        assertEquals(response.size(), 1);

    }

    @Test
    public void testgetAllWithFilterStringNotEmpty() throws ToptalException {

        doReturn(buildJogsList()).when(joggingRepository).findAll(any(Specification.class), any(Pageable.class));
        doReturn(updatedJoggingReponseDto).when(modelMapper)
                .map(new Jogging(1L, calendar, 30, 8000, "WEATHER desctiption", "temprature", "1.2", location, user), JoggingReponseDto.class);

        final List<JoggingReponseDto> response = joggingService.getAll(0, 10, "(id eq 1)");
        assertNotNull(response);
        assertEquals(response.size(), 1);

    }

    @Test
    public void testgetAllWithFilterStringNotEmptyWithIncorrectFilterCriteria() throws ToptalException {
        thrown.expectMessage(ToptalError.INCORRECT_FILTER_CRITERIA.getDescription());

        doReturn(buildJogsList()).when(joggingRepository).findAll(any(Specification.class), any(Pageable.class));
        doReturn(updatedJoggingReponseDto).when(modelMapper)
                .map(new Jogging(1L, calendar, 30, 8000, "WEATHER desctiption", "temprature", "1.2", location, user), JoggingReponseDto.class);

        final List<JoggingReponseDto> response = joggingService.getAll(0, 10, "(((id eq 1))");
        assertNotNull(response);
        assertEquals(response.size(), 1);
    }

    @Test
    public void testDeleteJog() throws ToptalException {
        doReturn(savedJogging).when(joggingRepository).findOne(1L);

        final Boolean response = joggingService.deleteJogging(1L);
        assertNotNull(response);
        assertEquals(response, true);
    }

    @Test
    public void testDeleteJogWithNotFoundJogException() throws ToptalException {
        thrown.expectMessage(ToptalError.JOGGING_NOT_FOUND.getDescription());
        doReturn(null).when(joggingRepository).findOne(1L);

        joggingService.deleteJogging(1L);
    }

    private Page<Jogging> buildJogsList() {
        final List<Jogging> jogs = new ArrayList<>();
        jogs.add(new Jogging(1L, calendar, 30, 8000, "WEATHER desctiption", "temprature", "1.2", location, user));
        final PageImpl<Jogging> jogsPage = new PageImpl<>(jogs, createPageRequest(10, 0), 10);
        return jogsPage;
    }

    private List<JoggingReponseDto> buildJogResponseList() {
        final List<JoggingReponseDto> jogs = new ArrayList<>();
        jogs.add(new JoggingReponseDto(1L, calendar.getTime(), 30, 8000, "WEATHER desctiption", "temprature", "1.2", locationDto, 5.5));
        return jogs;
    }
    private Pageable createPageRequest(final int pageSize, final int pageNumber) {
        return new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "id");
    }

    private Specification<Jogging> createSpecification() {
        final Specification<Jogging> jogSpec = new Specification<Jogging>() {

            @Override
            public Predicate toPredicate(final Root<Jogging> root, final CriteriaQuery<?> query, final CriteriaBuilder cb) {
                return cb.equal(root.get("id"), "1L");
            }
        };

        return jogSpec;

    }

}
