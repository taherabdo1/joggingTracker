package com.toptal.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.JoggingRequestDTO;
import com.toptal.demo.entities.Jogging;
import com.toptal.demo.entities.Location;
import com.toptal.demo.entities.User;
import com.toptal.demo.repositories.JoggingRepository;
import com.toptal.demo.repositories.LocationRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.service.weather.WeatherService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/joggings")
public class JoggingController {

    @Autowired
    WeatherService weatherService;

    @Autowired
    JoggingRepository joggingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ModelMapper modelMapper;

    // static Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    @ApiOperation(value = "create new jogging", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "the jogging created successfully") })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JoggingReponseDto addNewJogging(@RequestBody final JoggingRequestDTO joggingRequestDTO) {

        // get the current user
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String email = auth.getName(); // get logged in username
        final User user = userRepository.findOneByEmail(email).orElse(null);

        final Jogging jogging = modelMapper.map(joggingRequestDTO, Jogging.class);

        // add the user to the new jogging
        jogging.setUser(user);
        Location location = locationRepository.findByLocationName(jogging.getLocation().getLocationName());
        // if it is a new location for the system add it
        if (null == location) {
            location = locationRepository.save(jogging.getLocation());
        }
        jogging.setLocation(location);

        weatherService.getWeather(jogging);

        final Jogging result = joggingRepository.save(jogging);
        final JoggingReponseDto response = modelMapper.map(result, JoggingReponseDto.class);
        return response;
    }

    // private Jogging getJogging(JoggingRequestDTO joggingRequestDTO){
    // Jogging result = new Jogging();
    // result.setDate(joggingRequestDTO.getD);
    // }

}
