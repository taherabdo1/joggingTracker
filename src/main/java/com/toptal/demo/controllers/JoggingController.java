package com.toptal.demo.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.JoggingReponseDto;
import com.toptal.demo.dto.JoggingRequestDTO;
import com.toptal.demo.service.JoggingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/joggings")
public class JoggingController {

    @Autowired
    JoggingService joggingService;

    @ApiOperation(value = "create new jogging", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "the jogging created successfully") })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JoggingReponseDto addNewJogging(@Valid @RequestBody final JoggingRequestDTO joggingRequestDTO) throws ToptalException {

        // get the current user
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String email = auth.getName(); // get logged in email
        final JoggingReponseDto reponseDto = joggingService.addNewJogging(joggingRequestDTO, email);
        return reponseDto;
    }

    @ApiOperation(value = "get jogging by ID", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the jogging selected") })
    @RequestMapping(value = "/get/{joggingId}", method = RequestMethod.GET)
    public JoggingReponseDto getOne(@PathVariable("joggingId") final Long id) throws ToptalException {
        return joggingService.getOne(id);
    }

    @ApiOperation(value = "update pre-existing jogging", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the jogging updated successfully") })
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public JoggingReponseDto update(@Valid @RequestBody final JoggingRequestDTO joggingRequestDTO) throws ToptalException {
        return joggingService.update(joggingRequestDTO);
    }

    @ApiOperation(value = "get All Jogging for a user", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get all the joggings for a user") })
    @RequestMapping(value = "/getAllForUser/{userEmail}", method = RequestMethod.GET)
    public List<JoggingReponseDto> getAllForAuser(
            @Valid @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$") @PathVariable(name = "userEmail") final String userEmail,
            @RequestParam(required = false, name = "page") Integer page, @RequestParam(required = false, name = "size") Integer size,
            @RequestParam(required = false, name = "filterBY") final String filterString)
        throws ToptalException {
        if (size == null || page == null) {
            size = Integer.MAX_VALUE;
            page = 0;
        }

        return joggingService.getAllForAuser(userEmail, page, size, filterString);
    }

    @ApiOperation(value = "get All Jogging for the current user", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get all the joggings for the current user") })
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<JoggingReponseDto> getAll(@RequestParam(required = false, name = "page") Integer page,
            @RequestParam(required = false, name = "size") Integer size, @RequestParam(required = false, name = "filterBY") final String filterString)
        throws ToptalException {
        List<JoggingReponseDto> joggingsList;
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String email = auth.getName(); // get logged in userEmail

        if (size == null || page == null) {
            size = Integer.MAX_VALUE;
            page = 0;
        }

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            joggingsList = joggingService.getAll(page, size, filterString);
        } else {
            joggingsList = joggingService.getAllForAuser(email, page, size, filterString);
        }
        return joggingsList;
    }

    @ApiOperation(value = "delete jogging by ID", code = 200)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "the jogging selected") })
    @RequestMapping(value = "/delete/{joggingId}", method = RequestMethod.DELETE)
    public Response deleteJogging(@PathVariable("joggingId") final Long id) throws ToptalException {
        joggingService.deleteJogging(id);
        return Response.ok().build();
    }

}
