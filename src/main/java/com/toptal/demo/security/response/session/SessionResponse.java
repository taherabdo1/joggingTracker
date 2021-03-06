package com.toptal.demo.security.response.session;


import com.toptal.demo.dto.UserDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class SessionResponse extends OperationResponse {
  @ApiModelProperty(required = true, value = "")
  private UserDto item;
}
