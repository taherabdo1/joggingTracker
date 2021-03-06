package com.toptal.demo.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.toptal.demo.util.CustomDateDaySerializer;
import com.toptal.demo.util.CustomDateDeserializer;
import com.toptal.demo.util.CustomDateSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeedAndDistanceReportResponse {

    @ApiModelProperty(notes="distance in KM")
    @JsonProperty(value = "average_distance_per_day")
    private double distance;

    @ApiModelProperty(notes="speed in KM/h")
    @JsonProperty(value = "average_speed")
    private double speed;

    @Temporal(TemporalType.DATE)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonProperty(value = "start_Of_Week")
    @ApiModelProperty("yyyy-mm-dd , eg 2017-12-01")
    @JsonSerialize(using = CustomDateDaySerializer.class)
    private Date startOfWeek;

    @Temporal(TemporalType.DATE)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonProperty(value = "end_Of_Week")
    @ApiModelProperty("yyyy-mm-dd , eg 2017-12-08")
    @JsonSerialize(using = CustomDateDaySerializer.class)
    private Date endOfWeek;
    
}
