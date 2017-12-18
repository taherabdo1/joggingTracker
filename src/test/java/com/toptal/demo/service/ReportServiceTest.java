package com.toptal.demo.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.LocationDto;
import com.toptal.demo.dto.SpeedAndDistanceReportResponse;
import com.toptal.demo.entities.Jogging;
import com.toptal.demo.entities.Location;
import com.toptal.demo.entities.User;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.repositories.JoggingRepository;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {

    @InjectMocks
    ReportServiceImpl reportService;

    @Mock
    JoggingRepository jogRepository;

    @Mock
    ModelMapper modelMapper;

    @Rule
    public ExpectedException thrown = none();

    Calendar calendar = Calendar.getInstance();

    Calendar cal = Calendar.getInstance();

    Date START_DATE = null;

    Date END_DATE = null;
    final Location location = new Location(1L, 123.2f, 123.3f, "cairo", null);

    final User user = new User(1L, "test@demo.com", "password", 25, "user full name", Role.ROLE_USER, false, false, "cairo", null, null, null);

    final LocationDto locationDto = new LocationDto(123.2f, 123.3f, "cairo");

    final JoggingReponseDto joggingReponseDto = new JoggingReponseDto(1L, new Date(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", locationDto, 5.5);

    final JoggingReponseDto slowestJoggingReponseDto = new JoggingReponseDto(3L, new Date(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", locationDto,
            5.5);

    final JoggingReponseDto fastestJoggingReponseDto = new JoggingReponseDto(1L, new Date(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", locationDto,
            5.5);

    Jogging savedJogging = null;

    final String USER_EMAIL = "test@demo.com";

    @Before
    public void init() {
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);
        START_DATE = calendar.getTime();
        calendar.add(Calendar.DATE, 14);
        END_DATE = calendar.getTime();

        cal.setTime(new Date());

        savedJogging = new Jogging(1L, cal.getTime(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", location, user);

    }

    @Test
    public void testGetAgvSpeedAndDistanceForWeek() throws ToptalException {
        doReturn(buildJogsListForTwoWeeks()).when(jogRepository).findByUserEmailAndDateAfterAndDateBeforeOrderByDate(USER_EMAIL, START_DATE, END_DATE);
        final List<SpeedAndDistanceReportResponse> response = reportService.getAgvSpeedAndDistanceForWeek(USER_EMAIL, START_DATE, END_DATE);
        assertNotNull(response);
        assertTrue(response.size() > 0);

    }

    @Test
    public void testGetAgvSpeedAndDistanceForWeekIfThereIsNoJogReturnListwithZeros() throws ToptalException {
        thrown.expectMessage(ToptalError.USER_DOES_NOT_HAVE_JOGS.getDescription());
        doReturn(new ArrayList<Jogging>()).when(jogRepository).findByUserEmailAndDateAfterAndDateBeforeOrderByDate(USER_EMAIL, START_DATE, END_DATE);
        reportService.getAgvSpeedAndDistanceForWeek(USER_EMAIL, START_DATE, END_DATE);

    }

    @Test
    public void testGetAvgSpeedAndDistanceForTheLast2WeeksWeek() throws ToptalException {
        doReturn(buildJogsList()).when(jogRepository).findByUserEmailAndDateAfterAndDateBeforeOrderByDate(any(String.class), any(Date.class), any(Date.class));
        final List<SpeedAndDistanceReportResponse> response = reportService.getAvgSpeedAndDistanceForTheLast2WeeksWeek(USER_EMAIL);
        assertNotNull(response);
        assertTrue(response.size() == 2);

    }

    @Test
    public void testGetfastestSlowest() throws ToptalException {
        final List<Jogging> jogsResponse = buildJogsListForTwoWeeks();
        doReturn(slowestJoggingReponseDto).when(modelMapper).map(jogsResponse.get(2), JoggingReponseDto.class);
        doReturn(jogsResponse).when(jogRepository).findByUserEmailAndDateAfterAndDateBefore(any(String.class), any(Date.class), any(Date.class));
        final JoggingReponseDto response = reportService.getfastestSlowest(USER_EMAIL, "slowest", "week");
        assertTrue(response.getId() == jogsResponse.get(2).getId());

    }

    @Test
    public void testGetfastestInMonth() throws ToptalException {
        final List<Jogging> jogsResponse = buildJogsListForTwoWeeks();
        doReturn(fastestJoggingReponseDto).when(modelMapper).map(jogsResponse.get(0), JoggingReponseDto.class);
        doReturn(jogsResponse).when(jogRepository).findByUserEmailAndDateAfterAndDateBefore(any(String.class), any(Date.class), any(Date.class));
        final JoggingReponseDto response = reportService.getfastestSlowest(USER_EMAIL, "fastest", "month");
        assertTrue(response.getId() == jogsResponse.get(0).getId());

    }

    @Test
    public void testGetfastestSlowestWithBadParameters() throws ToptalException {
        thrown.expectMessage(ToptalError.JOGGING_VALIDATION_ERROR_NOT_BAD_REPORT_REQUEST_DATA.getDescription());

        final List<Jogging> jogsResponse = buildJogsListForTwoWeeks();
        doReturn(fastestJoggingReponseDto).when(modelMapper).map(jogsResponse.get(0), JoggingReponseDto.class);
        doReturn(jogsResponse).when(jogRepository).findByUserEmailAndDateAfterAndDateBefore(any(String.class), any(Date.class), any(Date.class));
        final JoggingReponseDto response = reportService.getfastestSlowest(USER_EMAIL, "fastestyy", "month");
        assertTrue(response.getId() == jogsResponse.get(0).getId());

    }

    @Test
    public void testGetfastestSlowestWithUserWithNoJogs() throws ToptalException {
        thrown.expectMessage(ToptalError.JOGGING_NO_JOGS_FOR_CURRENT_USER.getDescription());

        final List<Jogging> jogsResponse = buildJogsListForTwoWeeks();
        doReturn(new ArrayList<>()).when(jogRepository).findByUserEmailAndDateAfterAndDateBefore(any(String.class), any(Date.class), any(Date.class));
        final JoggingReponseDto response = reportService.getfastestSlowest(USER_EMAIL, "fastest", "year");
        assertTrue(response.getId() == jogsResponse.get(0).getId());

    }

    @Test
    public void testGetTheDayWithTheGreatestDistance() throws ToptalException {
        final List<Object[]> result = new ArrayList<>();
        final Date date = new Date();
        result.add(new Object[10]);
        result.get(0)[0] = date;
        doReturn(result).when(jogRepository).getMaxDistanceDateForAUser(USER_EMAIL);
        final Date response = reportService.getTheDayWithTheGreatestDistance(USER_EMAIL);
        assertTrue(response == result.get(0)[0]);

    }

    @Test
    public void testGetTheDayWithTheGreatestDistanceWithJOGGING_NO_JOGS_FOR_CURRENT_USER() throws ToptalException {
        thrown.expectMessage(ToptalError.JOGGING_NO_JOGS_FOR_CURRENT_USER.getDescription());

        final List<Object[]> result = new ArrayList<>();
        doReturn(result).when(jogRepository).getMaxDistanceDateForAUser(USER_EMAIL);
        final Date response = reportService.getTheDayWithTheGreatestDistance(USER_EMAIL);
        assertTrue(response == result.get(0)[0]);

    }

    @Test
    public void testGetTotalTimeRunning() throws ToptalException {
        final List<Jogging> jogs = buildJogsListForTwoWeeks();
        final double resultFromRepo = jogs.get(0).getDistance() + jogs.get(1).getDistance() + jogs.get(2).getDistance();
        doReturn(resultFromRepo).when(jogRepository).getSumTimeForAUser(USER_EMAIL);
        final Double result = jogRepository.getSumTimeForAUser(USER_EMAIL);// in hours

        assertTrue(result == resultFromRepo);

    }

    private List<Jogging> buildJogsList() {
        final List<Jogging> jogs = new ArrayList<>();
        jogs.add(savedJogging);
        return jogs;
    }

    private List<Jogging> buildJogsListForTwoWeeks() {
        final List<Jogging> jogs = new ArrayList<>();
        jogs.add(new Jogging(1L, cal.getTime(), 30, 4000, "WEATHER desctiption", "temprature", "1.2", location, user));
        jogs.add(new Jogging(2L, cal.getTime(), 45, 4000, "WEATHER desctiption", "temprature", "1.2", location, user));
        jogs.add(new Jogging(3L, cal.getTime(), 60, 4000, "WEATHER desctiption", "temprature", "1.2", location, user));
        return jogs;
    }
}
