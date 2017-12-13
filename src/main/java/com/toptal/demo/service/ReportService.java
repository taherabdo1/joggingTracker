package com.toptal.demo.service;

import java.util.Date;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.SpeedAndDistanceReportResponse;

public interface ReportService {

    public SpeedAndDistanceReportResponse getAgvSpeedAndDistanceForWeek(final String userEmail, Date startDate);

    public JoggingReponseDto getfastestSlowest(final String email, final String speed, final String period) throws ToptalException;

    public Date getTheDayWithTheGreatestDistance(final String email) throws ToptalException;

    public double getTotalTimeRunning(final String email) throws ToptalException;
}
