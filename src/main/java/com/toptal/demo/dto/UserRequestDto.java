package com.toptal.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String  email;
    @JsonProperty(value = "password", required = false)
    private String password;
    @ApiModelProperty(dataType = "string", allowableValues = "ROLE_ADMIN, ROLE_MANAGER, ROLE_USER")
    private Role role;
    
}
