package com.toptal.demo.service;

import java.util.Date;
import java.util.List;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.SpeedAndDistanceReportResponse;

public interface ReportService {

    public List<SpeedAndDistanceReportResponse> getAgvSpeedAndDistanceForWeek(final String userEmail, Date startDate, Date endDate) throws ToptalException;

    public List<SpeedAndDistanceReportResponse> getAvgSpeedAndDistanceForTheLast2WeeksWeek(final String userEmail);

    public JoggingReponseDto getfastestSlowest(final String email, final String speed, final String period) throws ToptalException;

    public Date getTheDayWithTheGreatestDistance(final String email) throws ToptalException;

    public double getTotalTimeRunning(final String email) throws ToptalException;
}
