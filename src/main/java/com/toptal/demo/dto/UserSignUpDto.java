
package com.toptal.demo.dto;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDto {

    @JsonFormat(pattern="^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")
    @ApiModelProperty(example = "userx@demo.com", required = true)
    @Pattern(regexp="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
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
