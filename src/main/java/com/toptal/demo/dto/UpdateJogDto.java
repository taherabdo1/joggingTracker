package com.toptal.demo.dto;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.toptal.demo.util.CustomDateDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJogDto {

    // to be used only with the update requests
    @NumberFormat
    @JsonProperty(value = "id", required = true)
    private long id;

    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonProperty(value = "date", required = false)
    @Future
    private Date date;

    // the period to run in minutes
    @Min(message = "can't be less than zero", value = 10)
    @JsonProperty(value = "periodInMinutes", required = false)
    private Integer periodInMinutes;

    // the distance to run
    @JsonProperty(value = "distance", required = false)
    @Min(message = "can't be less than zero", value = 10)
    private Integer distance;

    @JsonProperty(value = "location", required = false)
    private LocationDto location;

}
