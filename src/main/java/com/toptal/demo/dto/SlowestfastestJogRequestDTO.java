package com.toptal.demo.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlowestfastestJogRequestDTO {

    @JsonProperty(value = "speed", required = true,defaultValue="fastest")
    private String speed;
    
    @JsonProperty(value = "period", required = true,defaultValue="week")
    private String period;
}
