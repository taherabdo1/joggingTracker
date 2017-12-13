package com.toptal.demo.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.SpeedAndDistanceReportResponse;
import com.toptal.demo.service.ReportService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @ApiOperation(value = "get the average speed and distance for week for the current user", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get the average speed and distance for week for the current user") })
    @RequestMapping(value = "/averageSpeedAndDistancePerWeek", method = RequestMethod.GET)
    public SpeedAndDistanceReportResponse getAvgSpeedAndDistanceForWeek(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") final Date startDate) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String email = auth.getName(); // get logged in userEmail
        return reportService.getAgvSpeedAndDistanceForWeek(email, startDate);
    }

    @ApiOperation(value = "get the fastest/slowest jog in the week/month/year for the current user", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get the fastest/slowest jog in the week/month/year for the current user") })
    @RequestMapping(value = "/getFastestSlowestRun", method = RequestMethod.GET)
    public JoggingReponseDto getFastestSlowestRun(@ApiParam(allowableValues = "slowest,fastest") @RequestParam("speed") final String speed,
            @ApiParam(allowableValues = "week,month,year") @RequestParam("period") final String period)
        throws ToptalException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String email = auth.getName(); // get logged in userEmail
        final JoggingReponseDto response = reportService.getfastestSlowest(email, speed, period);
        response.setSpeed(response.getDistance() / 1000.00 / (response.getPeriodInMinutes() / 60));
        return response;
    }

    @ApiOperation(value = "get the day with the greatest distance ran", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get the day with the greatest distance ran") })
    @RequestMapping(value = "/getDayWithTheGreatestDistanceRan", method = RequestMethod.GET)
    public String getDayWithTheGreatestDistanceRan() throws ToptalException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String email = auth.getName(); // get logged in userEmail
        final Date response = reportService.getTheDayWithTheGreatestDistance(email);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(response);
    }

    /**
     * comparison of the previous and last week
     */

    // @ApiOperation(value = "comparison of the previous and last week", code = 200)
    // @ApiResponses(value = { @ApiResponse(code = 200, message = "comparison of the previous and last week") })
    // @RequestMapping(value = "/getDayWithTheGreatestDistanceRan", method = RequestMethod.GET)
    // public String getDayWithTheGreatestDistanceRan() throws ToptalException {
    // final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // final String email = auth.getName(); // get logged in userEmail
    // final Date response = reportService.getTheDayWithTheGreatestDistance(email);
    // final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //
    // return simpleDateFormat.format(response);
    // }
    //
    /**
     * the total time spent jogging
     */


    @ApiOperation(value = "get the total time spent jogging", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the total time spent jogging") })
    @RequestMapping(value = "/gettotalTimeSpentJogging", method = RequestMethod.GET)
    public double getTotalTimeRunning() throws ToptalException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String email = auth.getName(); // get logged in userEmail
        return reportService.getTotalTimeRunning(email);
    }
}