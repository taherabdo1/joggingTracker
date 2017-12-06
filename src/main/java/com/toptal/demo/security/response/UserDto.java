package com.toptal.demo.security.response;


import com.toptal.demo.entities.Location;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String  token;
    private Location addressLocation;
    private boolean activated = false;
    private boolean isBlocked = false;
    private String  email;
    private String role;
}
