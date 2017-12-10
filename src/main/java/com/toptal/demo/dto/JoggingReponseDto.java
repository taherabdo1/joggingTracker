package com.toptal.demo.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.toptal.demo.util.CustomDateDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoggingReponseDto {

    @JsonProperty(value = "id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value = "date", required = true)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date date;

    // the period to run in minutes
    @JsonProperty(value = "periodInMinutes", required = true)
    int periodInMinutes;

    // the distance to run
    @JsonProperty(value = "distance", required = false)
    private int distance;

    /**
     *  weather related info, clouds, temperature, windSpead
     */
    private String weatherDescription;

    private String temperature;

    private String windSpeed;
    
    private LocationDto location;
}
