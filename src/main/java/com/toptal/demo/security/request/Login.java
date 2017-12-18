package com.toptal.demo.security.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Login  {

  @Pattern(regexp="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")  
  @ApiModelProperty(example = "demo", required = true)
  private String email = "";

  @ApiModelProperty(example = "demo", required = true)
  @Size(min = 5, message = "password should be 5 chars minimum length")
  private String password = "";

}
