package com.toptal.demo.controllers;

import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.demo.entities.User;
import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.security.response.UserDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users")
public class UserController {

    static Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

	@Autowired
	UserRepository userRepository;
	
    @Autowired
    LoginAttemptRepository loginAttemptRepository;

    @GetMapping("/hello")
	public String helloWorld(){
		return "hello world";
	}
		
    @GetMapping("/getAll")
	public List<User> getAllUsers(){
    	System.out.println("number of users : "+ userRepository.findAll().toString());
		return (List<User>) userRepository.findAll();
	}

    @ApiOperation(value = "Re-activate the user", code = 204)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "the user re-activated successfully") })
    @RequestMapping(value = "/reacivate_user/{userId}", method = RequestMethod.PUT)
    public UserDto reactivate(@PathVariable("userId") final Long userId) {

        final User userToReactivate = userRepository.findOne(userId);
        userToReactivate.getLoginAttempt().setNumberOfTrials(0);
        userToReactivate.setBlocked(false);
        // re-initialise the login trials for this user
        loginAttemptRepository.save(userToReactivate.getLoginAttempt());
        final User reactivated = userRepository.save(userToReactivate);

        final UserDto userDto = mapper.map(reactivated, UserDto.class);

        return userDto;
    }
}
