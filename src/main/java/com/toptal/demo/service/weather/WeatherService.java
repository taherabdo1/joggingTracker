package com.toptal.demo.service.weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.entities.Jogging;
import com.toptal.demo.service.weather.dto.ListElement;
import com.toptal.demo.service.weather.dto.WeatherDto;
import com.toptal.demo.util.ToptalConfig;

import lombok.Data;

@Service
@Data
public class WeatherService {

    @Autowired
    ToptalConfig toptalconfig;

    @Autowired
    RestTemplate restTemplate;

    public void getWeather(final Jogging jogging) throws ToptalException {
        final String weatherApiURI = toptalconfig.getWeatherApiURI();
        final String resultMode = toptalconfig.getResultMode();
        final String apiToken = toptalconfig.getApiToken();
        final String uri = weatherApiURI + "?q={city}&mode={result_mode}&APPID={key}";
        // final RestTemplate restTemplate = new RestTemplate();

        try {
            final WeatherDto weatherDto = restTemplate.getForObject(uri, WeatherDto.class, jogging.getLocation().getLocationName(), resultMode, apiToken);
            final ListElement element = getListelement(weatherDto, jogging);
            jogging.setWeatherDescription(element.getWeather().get(0).getDescription());
            jogging.setTemperature(element.getMain().getTemp() + "");
            jogging.setWindSpeed(element.getWind().getSpeed() + "");
        } catch (final HttpClientErrorException e) {
            throw ToptalError.CITY_NOT_FOUND.buildException();
        } catch (final Exception exception) {
            throw ToptalError.EXTERNAL_SERVICE_ERROR.buildException();
        }
        // System.out.println(weatherDto);
    }

    private ListElement getListelement(final WeatherDto weatherDto, final Jogging jogging) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (final ListElement listElement : weatherDto.getList()) {
            try {
                if (format.parse(listElement.getDtTxt()).getTime() - jogging.getDate().getTime() >= 0
                        && format.parse(listElement.getDtTxt()).getTime() - jogging.getDate().getTime() <= 3 * 1000 * 60 * 60) {
                    return listElement;
                }
            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
