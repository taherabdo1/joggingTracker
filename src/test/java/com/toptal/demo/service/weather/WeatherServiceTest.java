package com.toptal.demo.service.weather;

import java.util.ArrayList;
import java.util.List;

import com.toptal.demo.service.weather.dto.City;
import com.toptal.demo.service.weather.dto.ListElement;
import com.toptal.demo.service.weather.dto.Main;
import com.toptal.demo.service.weather.dto.Weather;
import com.toptal.demo.service.weather.dto.WeatherDto;
import com.toptal.demo.service.weather.dto.Wind;

//@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {

    // @InjectMocks
    // WeatherService weatherService;
    //
    // @Mock
    // RestTemplate restTemplate;

    // @Test
    // public void testGetWeather() throws ToptalException {
    // doReturn(getWeatherDto()).when(restTemplate).getForObject(any(), any(), any(), any(), any());
    // final Jogging jogging = new Jogging(1L,new Date(),45,3000,null,null,null,null,null);
    // weatherService.getWeather(jogging);
    // assertNotNull(jogging.getWeatherDescription());
    // }

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
