package com.toptal.demo.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.toptal.demo.entities.Location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDto {
    private Long id;
    private String  token;
    private String city;
    private boolean activated = false;
    private boolean isBlocked = false;
    private String  email;
    private String role;
}
