package com.toptal.demo.service;

import java.text.DecimalFormat;
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
    public List<SpeedAndDistanceReportResponse> getAgvSpeedAndDistanceForWeek(final String userEmail, final Date startDate, final Date endDate) {

        final List<Jogging> jogs = jogRepository.findByUserEmailAndDateAfterAndDateBeforeOrderByDate(userEmail, startDate, endDate);

        final List<SpeedAndDistanceReportResponse> response = new ArrayList<>();

        final Calendar startCalendar = Calendar.getInstance();
        Calendar currentWeekStartCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        currentWeekStartCalendar = startCalendar;
        final Calendar endOfWeek = (Calendar) currentWeekStartCalendar.clone();
        if (startCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            endOfWeek.add(Calendar.DATE, 8 - startCalendar.get(Calendar.DAY_OF_WEEK));
        } else {
            endOfWeek.add(Calendar.DATE, 7);

        }

        List<Jogging> currentWeekJogs = new ArrayList<>();
        for (int i = 0; i < jogs.size(); i++) {
            //get the jogs of the current week
            if (jogs.get(i).getDate().after(currentWeekStartCalendar.getTime()) && jogs.get(i).getDate().before(endOfWeek.getTime())) {
                currentWeekJogs.add(jogs.get(i));
                continue;
            } else if (jogs.get(i).getDate().after(endOfWeek.getTime())) {
                //save the current week and initialise the next week
                final List<Double> speeds = new ArrayList<>();
                    for (final Jogging jog : currentWeekJogs) {
                        speeds.add(jog.getDistance() / 1000.000 / (jog.getPeriodInMinutes() / 60.000));// get the speed
                                                                                                       // in KM/hour
                    }
                    float totalSpeeds = 0f;
                    int totalDistance = 0;
                    for (int j = 0; j < speeds.size(); j++) {
                        totalSpeeds += speeds.get(j);
                        totalDistance += currentWeekJogs.get(j).getDistance();
                    }
                    SpeedAndDistanceReportResponse currentWeekReport = new SpeedAndDistanceReportResponse();
                    if (currentWeekJogs.size() != 0) {
                        final DecimalFormat df = new DecimalFormat("####.##");
                        currentWeekReport.setDistance(Double.parseDouble(df.format((double) totalDistance / currentWeekJogs.size() / 1000)));
                        currentWeekReport.setSpeed(Double.parseDouble(df.format(totalSpeeds / speeds.size())));
                    }
                    currentWeekReport.setStartOfWeek(currentWeekStartCalendar.getTime());
                    currentWeekReport.setEndOfWeek(endOfWeek.getTime());
                    response.add(currentWeekReport);
                    currentWeekReport = new SpeedAndDistanceReportResponse();

                    // re-intialize
                currentWeekStartCalendar.setTime(endOfWeek.getTime());
                // currentWeekStartCalendar.add(Calendar.DAY_OF_WEEK, 1);// the day after the end of the previous week
                    endOfWeek.setTime(currentWeekStartCalendar.getTime());
                    endOfWeek.add(Calendar.DAY_OF_WEEK, 7);
                    if (endOfWeek.after(endDate)) {
                        endOfWeek.setTime(endDate);
                    }
                    currentWeekJogs = new ArrayList<>();// empty the current week jogs list
                currentWeekJogs.add(jogs.get(i)); // add the current jog to the new week
                    continue;
            }

        }
        // handle the remaining for the last week
        if (currentWeekJogs.size() > 0) {
            final List<Double> speeds = new ArrayList<>();
            for (final Jogging jog : currentWeekJogs) {
                speeds.add(jog.getDistance() / 1000.000 / (jog.getPeriodInMinutes() / 60.000));// get the speed
                                                                                               // in KM/hour
            }
            float totalSpeeds = 0f;
            int totalDistance = 0;
            for (int j = 0; j < speeds.size(); j++) {
                totalSpeeds += speeds.get(j);
                totalDistance += jogs.get(j).getDistance();
            }
            SpeedAndDistanceReportResponse currentWeekReport = new SpeedAndDistanceReportResponse();
            if (currentWeekJogs.size() != 0) {
                final DecimalFormat df = new DecimalFormat("####.##");
                currentWeekReport.setDistance(Double.parseDouble(df.format((double) totalDistance / currentWeekJogs.size() / 1000)));
                currentWeekReport.setSpeed(Double.parseDouble(df.format(totalSpeeds / speeds.size())));
            }
            currentWeekReport.setStartOfWeek(currentWeekStartCalendar.getTime());
            currentWeekReport.setEndOfWeek(endOfWeek.getTime());
            response.add(currentWeekReport);
            currentWeekReport = new SpeedAndDistanceReportResponse();
            currentWeekJogs = new ArrayList<>();
        }
        return response;

    }

    @Override
    public List<SpeedAndDistanceReportResponse> getAvgSpeedAndDistanceForTheLast2WeeksWeek(final String userEmail) {

        SpeedAndDistanceReportResponse firstWeek = new SpeedAndDistanceReportResponse();
        SpeedAndDistanceReportResponse secondWeek = new SpeedAndDistanceReportResponse();
        final List<SpeedAndDistanceReportResponse> response = new ArrayList();
        final List<Jogging> firstWeekJogs = new ArrayList<>();
        final List<Jogging> secondWeekJogs = new ArrayList<>();

        final Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date());
        startCalendar.add(Calendar.DAY_OF_WEEK, -14);// two week ago
        final Calendar intermediatDate = Calendar.getInstance();
        intermediatDate.setTime(startCalendar.getTime());
        intermediatDate.add(Calendar.DAY_OF_WEEK, 7);

        final List<Jogging> jogs = jogRepository.findByUserEmailAndDateAfterAndDateBeforeOrderByDate(userEmail, startCalendar.getTime(), endCalendar.getTime());

        for (final Jogging jog : jogs) {
            if (jog.getDate().before(intermediatDate.getTime())) {
                firstWeekJogs.add(jog);
            } else {
                secondWeekJogs.add(jog);
            }
        }
        // calculate the averages
        firstWeek = calculateAvg(firstWeekJogs);
        firstWeek.setStartOfWeek(startCalendar.getTime());
        firstWeek.setEndOfWeek(intermediatDate.getTime());

        secondWeek = calculateAvg(secondWeekJogs);
        secondWeek.setStartOfWeek(intermediatDate.getTime());
        secondWeek.setEndOfWeek(endCalendar.getTime());

        response.add(firstWeek);
        response.add(secondWeek);

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

        if (speed.equalsIgnoreCase("fastest")) {
            return modelMapper.map(maxSpeedJog, JoggingReponseDto.class);
        } else if (speed.equalsIgnoreCase("slowest")) {
            return modelMapper.map(minSpeedJog, JoggingReponseDto.class);
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
        final Double result = jogRepository.getSumTimeForAUser(email);// in hours
        if (result == null) {
            return 0;
        }
        final DecimalFormat df = new DecimalFormat("####.##");

        return Double.parseDouble(df.format(result / 60));
    }

    private SpeedAndDistanceReportResponse calculateAvg(final List<Jogging> jogs) {
        final SpeedAndDistanceReportResponse currentWeekReport = new SpeedAndDistanceReportResponse();
        if (jogs.size() > 0) {
            final List<Double> speeds = new ArrayList<>();
            for (final Jogging jog : jogs) {
                speeds.add(jog.getDistance() / 1000.000 / (jog.getPeriodInMinutes() / 60.000));// get the speed
                                                                                               // in KM/hour
            }
            float totalSpeeds = 0f;
            int totalDistance = 0;
            for (int j = 0; j < speeds.size(); j++) {
                totalSpeeds += speeds.get(j);
                totalDistance += jogs.get(j).getDistance();
            }
            if (jogs.size() != 0) {
                final DecimalFormat df = new DecimalFormat("####.##");
                currentWeekReport.setDistance(Double.parseDouble(df.format((double) totalDistance / jogs.size() / 1000)));
                currentWeekReport.setSpeed(Double.parseDouble(df.format(totalSpeeds / speeds.size())));
            }
        } else {
            currentWeekReport.setDistance(0);
            currentWeekReport.setSpeed(0);
        }

        return currentWeekReport;
    }

}
