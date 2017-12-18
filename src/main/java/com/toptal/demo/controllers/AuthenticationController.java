package com.toptal.demo.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserSignUpDto;
import com.toptal.demo.security.request.Login;
import com.toptal.demo.security.response.session.SessionResponse;
import com.toptal.demo.service.AuthenicationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = { "Authentication" })
public class AuthenticationController {

    @Autowired
    AuthenicationService authenicationService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Will return a security token, which must be passed in every request", response = SessionResponse.class) })
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ResponseBody
    public void authenticate(@Valid @RequestBody final Login login, final HttpServletRequest request, final HttpServletResponse response) {
    }

    @ApiResponses(value = { @ApiResponse(code = 201, message = "", response = SessionResponse.class) })
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public SessionResponse signup(@Valid @RequestBody final UserSignUpDto userInfo)
        throws ToptalException {
        return authenicationService.signup(userInfo);
    }

    @ApiOperation(value = "Activate the user by activation key sent by mail", code = 204)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "the user activated successfully") })
    @RequestMapping(value = "/activate/{activateKey}", method = RequestMethod.GET)
    public Response activateAccount(@PathVariable(value = "activateKey") final String activateKey) throws ToptalException {
        authenicationService.activateAccount(activateKey);
        return Response.status(Status.NO_CONTENT).entity("User acivated successfully").build();
    }
}
