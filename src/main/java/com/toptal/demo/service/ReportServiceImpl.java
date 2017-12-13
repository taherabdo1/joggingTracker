package com.toptal.demo.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.SpeedAndDistanceReportResponse;
import com.toptal.demo.entities.Jogging;
import com.toptal.demo.repositories.JoggingRepository;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    JoggingRepository jogRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public SpeedAndDistanceReportResponse getAgvSpeedAndDistanceForWeek(final String userEmail, Date startDate) {
        final SpeedAndDistanceReportResponse response = new SpeedAndDistanceReportResponse();
        // if the start date is not given then get the last week
        final Calendar calendar = Calendar.getInstance();
        Date endDate = null;
        if (startDate == null) {
            endDate = new Date();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            startDate = calendar.getTime();
        } else {
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            endDate = calendar.getTime();
        }
        final List<Jogging> jogs = jogRepository.findByUserEmailAndDateAfterAndDateBefore(userEmail, startDate, endDate);
        final List<Double> speeds = new ArrayList<>();
        for (final Jogging jog : jogs) {
            speeds.add(jog.getDistance() / 1000.000 / (jog.getPeriodInMinutes() / 60.000));// get the speed in KM/hour
        }
        float totalSpeeds = 0f;
        int totalDistance = 0;
        for (int i = 0; i < speeds.size(); i++) {
            totalSpeeds += speeds.get(i);
            totalDistance += jogs.get(i).getDistance();
        }
        if (jogs.size() != 0) {
            response.setDistance(totalDistance / jogs.size());
            response.setSpeed(totalSpeeds / speeds.size());
        }
        response.setStartOfWeek(startDate);

        return response;

    }

    @Override
    public JoggingReponseDto getfastestSlowest(final String email, final String speed, final String period) throws ToptalException {

        final Date endDate = new Date();
        Date startDate = null;
        final Calendar calendar = Calendar.getInstance();
        switch (period) {
            case "week":
                calendar.setTime(endDate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                startDate = calendar.getTime();
                break;
            case "month":
                calendar.setTime(endDate);
                calendar.add(Calendar.MONTH, -1);
                startDate = calendar.getTime();
                break;
            case "year":
                calendar.setTime(endDate);
                calendar.add(Calendar.YEAR, -1);
                startDate = calendar.getTime();
                break;
        }

        final List<Jogging> jogs = jogRepository.findByUserEmailAndDateAfterAndDateBefore(email, startDate, endDate);
        if (jogs.size() == 0 || jogs == null) {
            throw ToptalError.JOGGING_NO_JOGS_FOR_CURRENT_USER.buildException();
        }

        Jogging maxSpeedJog = jogs.get(0);
        Jogging minSpeedJog = jogs.get(0);
        double maxSpeed = calculateSpeed(jogs.get(0));
        double minSpeed = calculateSpeed(jogs.get(0));
        for (int i = 1; i < jogs.size(); i++) {
            if (calculateSpeed(jogs.get(i)) > maxSpeed) {
                maxSpeed = calculateSpeed(jogs.get(i));
                maxSpeedJog = jogs.get(i);
            } else if (calculateSpeed(jogs.get(i)) < minSpeed) {
                minSpeed = calculateSpeed(jogs.get(i));
                minSpeedJog = jogs.get(i);
            }
        }

        if(speed.equalsIgnoreCase("fastest")){
            return modelMapper.map(maxSpeedJog,JoggingReponseDto.class);
        }else if(speed.equalsIgnoreCase("slowest")){
            return  modelMapper.map(minSpeedJog,JoggingReponseDto.class);
        }
        throw ToptalError.JOGGING_VALIDATION_ERROR_NOT_BAD_REPORT_REQUEST_DATA.buildException();
    }


    private double calculateSpeed(final Jogging jog) {
        return jog.getDistance() / 1000.00 / ((double) jog.getPeriodInMinutes() / 60);
    }

    @Override
    public Date getTheDayWithTheGreatestDistance(final String email) throws ToptalException {
        List<Object[]> result = new ArrayList<>();
        result = jogRepository.getMaxDistanceDateForAUser(email);
        if (result.size() == 0 || result.get(0)[0] == null) {
            throw ToptalError.JOGGING_NO_JOGS_FOR_CURRENT_USER.buildException();
        }
        final Date resultDate = (Date) result.get(0)[0];
        return resultDate;
    }

    @Override
    public double getTotalTimeRunning(final String email) throws ToptalException {
        final Object result = jogRepository.getSumTimeForAUser(email) / 60;// in hours
        if (result == null) {
            return 0;
        }
        return (Double) result;
    }

}
