package com.toptal.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.core.Response;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
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
    public JoggingReponseDto addNewJogging(@RequestBody final JoggingRequestDTO joggingRequestDTO) throws ToptalException {

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

        // get all joggings to check for overlaps
        final List<Jogging> all = joggingRepository.findByUserEmail(user.getEmail());
        final Calendar calendar = Calendar.getInstance();
        for (final Jogging run : all) {
            calendar.setTimeInMillis(run.getDate().getTime());
            calendar.add(Calendar.MINUTE, run.getTime());
            if (run.getDate().before(jogging.getDate()) && calendar.getTime().after(jogging.getDate())
                    || run.getDate().getTime() == jogging.getDate().getTime()) {
                throw ToptalError.JOGGING_OVERLAPING.buildException();
            }
        }
        final Jogging result = joggingRepository.save(jogging);
        final JoggingReponseDto response = modelMapper.map(result, JoggingReponseDto.class);
        return response;
    }

    @ApiOperation(value = "get jogging by ID", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the jogging selected") })
    @RequestMapping(value = "/get/{joggingId}", method = RequestMethod.GET)
    public JoggingReponseDto getOne(@PathVariable("joggingId") final Long id) throws ToptalException {
        final Jogging selected = joggingRepository.findOne(id);
        if (selected == null) {
            throw ToptalError.JOGGING_NOT_FOUND.buildException();
        }
        final JoggingReponseDto response = modelMapper.map(selected, JoggingReponseDto.class);
        return response;
    }

    @ApiOperation(value = "update pre-existing jogging", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the jogging updated successfully") })
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public JoggingReponseDto update(@RequestBody final JoggingRequestDTO joggingRequestDTO) throws ToptalException {
        final Jogging selected = joggingRepository.findOne(joggingRequestDTO.getId());
        if (selected == null) {
            throw ToptalError.JOGGING_NOT_FOUND.buildException();
        }
        final Jogging jogging = modelMapper.map(joggingRequestDTO, Jogging.class);
        final User user = userRepository.findOne(jogging.getUser().getId());
        final Location location = locationRepository.findByLocationName(jogging.getLocation().getLocationName());
        jogging.setUser(user);
        jogging.setLocation(location);

        // if a new location is set
        if (!jogging.getLocation().getLocationName().equalsIgnoreCase(selected.getLocation().getLocationName())) {
            weatherService.getWeather(jogging);
        }

        // get all joggings to check for overlaps
        final List<Jogging> all = joggingRepository.findByUserEmail(user.getEmail());
        final Calendar calendar = Calendar.getInstance();
        for (final Jogging run : all) {
            calendar.setTimeInMillis(run.getDate().getTime());
            calendar.add(Calendar.MINUTE, run.getTime());
            if (run.getDate().before(jogging.getDate()) && calendar.getTime().after(jogging.getDate())
                    || run.getDate().getTime() == jogging.getDate().getTime()) {
                throw ToptalError.JOGGING_OVERLAPING.buildException();
            }
        }
        final Jogging result = joggingRepository.save(jogging);
        final JoggingReponseDto response = modelMapper.map(result, JoggingReponseDto.class);
        return response;
    }

    @ApiOperation(value = "get All Jogging for a user", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get all the joggings for a user") })
    @RequestMapping(value = "/getAllForUser/{userEmail}", method = RequestMethod.GET)
    public List<JoggingReponseDto> getAllForAuser(
            @Valid @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$") @PathVariable(name = "userEmail") final String userEmail)
        throws ToptalException {
        final List<Jogging> joggingsList = joggingRepository.findByUserEmail(userEmail);
        final List<JoggingReponseDto> response = new ArrayList<>();
        for (final Jogging jogging : joggingsList) {
            response.add(modelMapper.map(jogging, JoggingReponseDto.class));
        }
        return response;
    }

    @ApiOperation(value = "get All Jogging for the current user", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get all the joggings for the current user") })
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<JoggingReponseDto> getAll() {
        List<Jogging> joggingsList;
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String email = auth.getName(); // get logged in userEmail
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            joggingsList = Lists.newArrayList(joggingRepository.findAll());
        } else {
            joggingsList = joggingRepository.findByUserEmail(email);
        }

        final List<JoggingReponseDto> response = new ArrayList<>();
        for (final Jogging jogging : joggingsList) {
            response.add(modelMapper.map(jogging, JoggingReponseDto.class));
        }
        return response;

    }

    @ApiOperation(value = "delete jogging by ID", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the jogging selected") })
    @RequestMapping(value = "/delete/{joggingId}", method = RequestMethod.DELETE)
    public Response deleteJogging(@PathVariable("joggingId") final Long id) throws ToptalException {
        final Jogging selected = joggingRepository.findOne(id);
        if (selected == null) {
            throw ToptalError.JOGGING_NOT_FOUND.buildException();
        }
        joggingRepository.delete(selected);
        return Response.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    void handleBadRequests(final HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Please try again and with a non empty string as 'name'");
    }

}
