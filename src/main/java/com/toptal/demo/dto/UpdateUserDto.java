package com.toptal.demo.dto;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.toptal.demo.entities.enums.Role;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    @NumberFormat
    @JsonProperty(value = "id", required = true)
    private Long id;

    @JsonProperty(value = "city", required = false)
    private String city;

    @JsonProperty(value = "activated", required = false)
    private Boolean activated;

    @JsonProperty(value = "isBlocked", required = false)
    private Boolean isBlocked;

    @JsonProperty(value = "email", required = false)
    private String email;

    @JsonProperty(value = "role", required = false)
    @ApiModelProperty(dataType = "string", allowableValues = "ROLE_ADMIN, ROLE_MANAGER, ROLE_USER")
    private Role role;

}
