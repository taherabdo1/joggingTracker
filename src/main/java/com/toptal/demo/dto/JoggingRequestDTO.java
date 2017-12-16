package com.toptal.demo.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.toptal.demo.util.CustomDateDeserializer;

import io.swagger.annotations.ApiModelProperty;
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

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(value = "date", required = true)
    @JsonDeserialize(using = CustomDateDeserializer.class)    
    @ApiModelProperty(value = "YYYY-MM-DD, e.g. 2015-01-16")
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
