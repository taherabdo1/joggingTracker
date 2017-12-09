package com.toptal.demo.dto;

import java.util.Date;

import org.springframework.format.annotation.NumberFormat;

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

    //to be used only with the update requests
    @NumberFormat
    @JsonProperty(value = "id", required = false)
    private long id;
    
    @JsonProperty(value = "date", required = true)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date date;

    // the distance to run
    @JsonProperty(value = "distance", required = false)
    private int distance;

    // the time(period) to run in minutes
    @JsonProperty(value = "time", required = true)
    private int time;

    @JsonProperty(value = "location", required = true)
    private LocationDto location;
}
