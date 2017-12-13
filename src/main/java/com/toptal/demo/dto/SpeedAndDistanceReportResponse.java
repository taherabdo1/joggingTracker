package com.toptal.demo.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.toptal.demo.util.CustomDateDeserializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeedAndDistanceReportResponse {

    @JsonProperty(value = "average_distance")
    private int distance;

    @JsonProperty(value = "average_speed")
    private double speed;

    @Temporal(TemporalType.DATE)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonProperty(value = "start_Of_Week")
    @ApiModelProperty("yyyy-mm-dd , eg 2017-12-01")
    private Date startOfWeek;
}
