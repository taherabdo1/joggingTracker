package com.toptal.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.demo.entities.User;
import com.toptal.demo.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
    @GetMapping("/hello")
	public String helloWorld(){
		return "hello world";
	}
		
    @GetMapping("/getAll")
	public List<User> getAllUsers(){
    	System.out.println("number of users : "+ userRepository.findAll().toString());
		return (List<User>) userRepository.findAll();
	}
}
