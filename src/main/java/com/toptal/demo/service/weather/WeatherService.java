package com.toptal.demo.service.weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.toptal.demo.entities.Jogging;
import com.toptal.demo.service.weather.dto.ListElement;
import com.toptal.demo.service.weather.dto.WeatherDto;

@Service
public class WeatherService {

    @Value("${weather.api.rest.uri}")
    String weatherApiURI;

    @Value("${weather.api.rest.result.mode}")
    String resultMode;

    @Value("${weather.api.rest.token}")
    String apiToken;

    public void getWeather(final Jogging jogging) {
        final String uri = weatherApiURI + "?q={city}&mode={result_mode}&APPID={key}";
        final RestTemplate restTemplate = new RestTemplate();

        try {
            final WeatherDto weatherDto = restTemplate.getForObject(uri, WeatherDto.class, jogging.getLocation().getLocationName(), resultMode, apiToken);
            final ListElement element = getListelement(weatherDto, jogging);
            jogging.setWeatherDescription(element.getWeather().get(0).getDescription());
            jogging.setTemperature(element.getMain().getTemp() + "");
            jogging.setWindSpeed(element.getWind().getSpeed() + "");
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
        // System.out.println(weatherDto);
    }

    private ListElement getListelement(final WeatherDto weatherDto, final Jogging jogging) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (final ListElement listElement : weatherDto.getList()) {
            try {
                if (format.parse(listElement.getDtTxt()).getTime() - jogging.getDate().getTime() >= 0
                        && format.parse(listElement.getDtTxt()).getTime() - jogging.getDate().getTime() < 3 * 1000 * 60 * 60) {
                    return listElement;
                }
            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
