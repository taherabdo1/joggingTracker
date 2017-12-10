package com.toptal.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.toptal.demo.entities.enums.Role;

import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserRequestDto {

  
    private String city;
    private boolean activated = false;
    private boolean isBlocked = false;
    @JsonProperty(value = "email", required = true)
    private String  email;
    @ApiModelProperty(dataType = "string", allowableValues = "ROLE_ADMIN, ROLE_MANAGER, ROLE_USER")
    private Role role;

}
