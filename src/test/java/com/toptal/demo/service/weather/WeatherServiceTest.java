package com.toptal.demo.service.weather;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.entities.Jogging;
import com.toptal.demo.entities.Location;
import com.toptal.demo.service.weather.dto.City;
import com.toptal.demo.service.weather.dto.ListElement;
import com.toptal.demo.service.weather.dto.Main;
import com.toptal.demo.service.weather.dto.Weather;
import com.toptal.demo.service.weather.dto.WeatherDto;
import com.toptal.demo.service.weather.dto.Wind;
import com.toptal.demo.util.ToptalConfig;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(properties = { "weatherApiURI=http://api.openweathermap.org/data/2.5/forecast", "resultMode=json",
        "apiToken=aad44e3e5e50acde437421cee27ce918" })
public class WeatherServiceTest {

    @InjectMocks
    WeatherService weatherService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ToptalConfig toptalConfig;

    @Test
    public void testGetWeather() throws ToptalException, ParseException {
        doReturn(getWeatherDto()).when(restTemplate).getForObject(any(), any(), any(), any(), any());
        doReturn("token").when(toptalConfig).getApiToken();
        doReturn("json").when(toptalConfig).getResultMode();
        doReturn("url").when(toptalConfig).getWeatherApiURI();
        doReturn(getWeatherDto()).when(restTemplate).getForObject(any(), any(), any(), any(), any());

        final Location location = new Location();
        location.setId(1l);
        location.setLocationName("cairo");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse("2017-12-12 15:10:00"));
        final Jogging jogging = new Jogging(1L, calendar, 45, 3000, null, null, null, null, null);
        jogging.setLocation(location);
        weatherService.getWeather(jogging);
        assertNotNull(jogging.getWeatherDescription());
    }

    private WeatherDto getWeatherDto() {
        final WeatherDto weatherDto = new WeatherDto();
        final List<ListElement> weatherElements = new ArrayList<>();
        final City city = new City();
        city.setName("cairo");
        weatherElements.add(getListElement());
        weatherDto.setCity(city);
        weatherDto.setList(weatherElements);
        return weatherDto;
    }

    private ListElement getListElement() {
        final List<Weather> weather = new ArrayList<>();

        final Weather weatherElement = new Weather();
        weatherElement.setDescription("cloudy");
        weather.add(weatherElement);

        final ListElement element = new ListElement();
        element.setWeather(weather);
        element.setDtTxt("2017-12-12 15:10:00");

        final Main main = new Main();
        main.setTemp(216.0);
        element.setMain(main);

        final Wind wind = new Wind();
        wind.setSpeed(3.9);
        element.setWind(wind);

        element.setWeather(weather);

        return element;
    }
}
