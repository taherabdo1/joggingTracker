package com.toptal.demo.controllers;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.dto.UserRequestDto;
import com.toptal.demo.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String helloWorld() {
        return "hello world";
    }

    @ApiOperation(value = "get all users", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get all the users") })
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<UserDto> getAllUsers(
            @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "size must be positive natual number ") @RequestParam(name = "size") final int pageSize,
            @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "page number must be greater than or equal 0") @RequestParam(name = "pageNumber") final int pageNumer)
        throws ToptalException {
        return userService.getAll(pageSize, pageNumer);
    }

    @ApiOperation(value = "get user by email", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get user by email") })
    @RequestMapping(value = "/getByEmail/{userEmail}", method = RequestMethod.GET)
    public UserDto getUserByEmail(@PathVariable(name = "userEmail") final String email) throws ToptalException {
        return userService.getUserByemail(email);
    }

    @ApiOperation(value = "get user by ID", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get user by ID") })
    @RequestMapping(value = "/getById/{ID}", method = RequestMethod.GET)
    public UserDto getUserByID(@PathVariable(name = "ID") final long id) throws ToptalException {
        return userService.getUserByID(id);
    }

    @ApiOperation(value = "delete user", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the user deleted successfully") })
    @RequestMapping(value = "/delete/{ID}", method = RequestMethod.DELETE)
    public Response deleteUserByID(@PathVariable(name = "ID") final long id) throws ToptalException {
        userService.delete(id);
        return Response.ok().build();
    }

    @ApiOperation(value = "update user", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the user updated successfully") })
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public UserDto updateUser(@RequestBody final UserRequestDto userDto) throws ToptalException {
        return userService.update(userDto);
    }

    @ApiOperation(value = "Re-activate the user", code = 204)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "the user re-activated successfully") })
    @RequestMapping(value = "/reacivate_user/{userId}", method = RequestMethod.PUT)
    public UserDto reactivate(@PathVariable("userId") final Long userId) {
        return userService.reactivate(userId);
    }

}
