package com.toptal.demo.security.response.session;


import lombok.*;
import java.util.*;
import io.swagger.annotations.ApiModelProperty;

@Data
public class SessionItem {
    private String  token;
    private Long  userId;
    private String  email;
    private String role;
    private String password;
}
