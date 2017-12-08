package com.toptal.demo.controllers;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.toptal.demo.dto.UserDto;
import com.toptal.demo.entities.ActivateKey;
import com.toptal.demo.entities.LoginAttempt;
import com.toptal.demo.entities.User;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.repositories.ActivateKeyRepository;
import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.security.request.Login;
import com.toptal.demo.security.response.session.OperationResponse.ResponseStatusEnum;
import com.toptal.demo.security.response.session.SessionResponse;
import com.toptal.demo.service.EmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = { "Authentication" })
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivateKeyRepository activateKeyRepository;

    @Autowired
    LoginAttemptRepository loginAttemptRepository;

    @Autowired
    EmailService emailService;

    @Value("${running.host}")
    String hostName;

    @Value("${activate.mail.message}")
    String message;

    @Value("${activate.mail.message.footer}")
    String messageFooter;

    private final String WELCOME_EMAIL_SUBJECT = "Welcome! confirm your email";

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Will return a security token, which must be passed in every request", response = SessionResponse.class) })
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SessionResponse newSession(@RequestBody final Login login, final HttpServletRequest request, final HttpServletResponse response) {
        // System.out.format("\n /Session Called username=%s\n",
        // login.getUsername());
        // DriverDO driver =
        // driverRepository.findOneByUsernameAndPassword(login.getUsername(),
        // login.getPassword()).orElse(null);
        // SessionResponse resp = new SessionResponse();
        // SessionItem sessionItem = new SessionItem();
        // if (driver != null){
        // System.out.format("\n /User Details=%s\n", driver.getUsername());
        // sessionItem.setToken("xxx.xxx.xxx");
        // sessionItem.setUserId(driver.getId());
        // sessionItem.setUsername(driver.getUsername());
        // //sessionItem.setRole(user.getRole());
        //
        // resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
        // resp.setOperationMessage("Dummy Login Success");
        // resp.setItem(sessionItem);
        // }
        // else{
        // resp.setOperationStatus(ResponseStatusEnum.ERROR);
        // resp.setOperationMessage("Login Failed");
        // }
        // return resp;
        return null;
    }

    @ApiResponses(value = { @ApiResponse(code = 201, message = "", response = SessionResponse.class) })
    @RequestMapping(value = "/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SessionResponse signup(@RequestBody final Login credentials, final HttpServletRequest request, final HttpServletResponse response) {
        final User newUser = new User();
        newUser.setActivated(false);
        newUser.setPassword(credentials.getPassword());
        newUser.setEmail(credentials.getEmail());
        newUser.setRole(Role.ROLE_USER);
        final User createdUser = userRepository.save(newUser);
        final SessionResponse resp = new SessionResponse();
        if (null != createdUser.getId()) {
            resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
            resp.setOperationMessage("User signed up successflly");
            final UserDto sessionItem = new UserDto();
            sessionItem.setRole(createdUser.getRole().toString());
            sessionItem.setId(createdUser.getId());
            sessionItem.setEmail(createdUser.getEmail());
            resp.setItem(sessionItem);

            // generate UUID for the new created user
            final String userActivationKey = UUID.randomUUID().toString();
            final ActivateKey activateKey = new ActivateKey();
            activateKey.setUser(createdUser);
            activateKey.setKeySerial(userActivationKey);

            final ActivateKey createdActivateKey = activateKeyRepository.save(activateKey);

            // add the new user to the login attempt tracker table
            final LoginAttempt loginAttempt = new LoginAttempt();
            loginAttempt.setDate(new Date());
            loginAttempt.setNumberOfTrials(0);
            loginAttempt.setUser(createdUser);
            loginAttemptRepository.save(loginAttempt);
            // send mail to activate the account
            if (createdActivateKey.getId() != null) {
                emailService.send(createdUser.getEmail(), WELCOME_EMAIL_SUBJECT, buildVerificationMail(createdActivateKey.getKeySerial()));
            }

        } else {
            resp.setOperationStatus(ResponseStatusEnum.ERROR);
            resp.setOperationMessage("SignUp Failed");
        }
        return resp;
    }

    @ApiOperation(value = "Activate the user by activation key sent by mail", code = 204)
    @ApiResponses(value = { @ApiResponse(code = 204, message = "the user activated successfully") })
    @RequestMapping(value = "/activate/{activateKey}", method = RequestMethod.GET)
    public Response activateAccount(@PathVariable(value = "activateKey") final String activateKey) {
        final ActivateKey activateKeyObj = activateKeyRepository.findByKeySerial(activateKey);
        final User user = userRepository.findOne(activateKeyObj.getUser().getId());
        user.setActivated(true);
        userRepository.save(user);
        return Response.status(Status.NO_CONTENT).build();
    }

    private String buildVerificationMail(final String uuid) {
        final StringBuilder textBiulder = new StringBuilder();
        textBiulder.append(message);
        textBiulder.append(hostName);
        textBiulder.append("activate/" + uuid + "\n");
        textBiulder.append(messageFooter);

        return textBiulder.toString();
    }
}
