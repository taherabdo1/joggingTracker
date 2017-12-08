package com.toptal.demo.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @JsonProperty(value = "latitude", required = false)
    private float latitutde;

    @JsonProperty(value = "longtude", required = false)
    private float longtude;

    @JsonProperty(value = "locationName", required = true)
    @NotNull
    @NotBlank
    private String locationName;

}
