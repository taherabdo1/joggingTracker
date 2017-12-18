
package com.toptal.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDto {

    @JsonFormat(pattern="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @ApiModelProperty(example = "userx@demo.com", required = true)
    private String email = "";

    @ApiModelProperty(example = "password", required = true)
    private String password = "";

    @ApiModelProperty(example = "cairo", required = false)
    private String city = "";

    @ApiModelProperty(example = "20", required = false)
    private int age;

    @ApiModelProperty(example = "taher Abdelmohsen", required = true)
    private String fullName;
}
