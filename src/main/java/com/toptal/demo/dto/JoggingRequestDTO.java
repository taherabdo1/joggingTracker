package com.toptal.demo.dto;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;

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
public class JoggingRequestDTO {

    @JsonDeserialize(using = CustomDateDeserializer.class)    
    @Future
    private Date date;

    // the period to run in minutes
    @Min(message="can't be less than zero", value = 10)
    @JsonProperty(value = "periodInMinutes", required = true)
    int periodInMinutes;

    // the distance to run
    @JsonProperty(value = "distance", required = false)
    @Min(message="can't be less than zero", value = 1000)
    private int distance;

    @JsonProperty(value = "location", required = true)
    private LocationDto location;
    
}
