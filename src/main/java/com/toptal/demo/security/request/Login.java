package com.toptal.demo.security.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Login  {

  @ApiModelProperty(example = "demo", required = true)
  private String email = "";

  @ApiModelProperty(example = "demo", required = true)
  private String password = "";

}
