package com.toptal.demo.service;

import java.util.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.dto.UserSignUpDto;
import com.toptal.demo.entities.ActivateKey;
import com.toptal.demo.entities.LoginAttempt;
import com.toptal.demo.entities.User;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.repositories.ActivateKeyRepository;
import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.security.response.session.OperationResponse.ResponseStatusEnum;
import com.toptal.demo.security.response.session.SessionResponse;

@Service
public class AuthenicationServiceImpl implements AuthenicationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivateKeyRepository activateKeyRepository;

    @Autowired
    LoginAttemptRepository loginAttemptRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailServiceImpl emailService;

    @Value("${running.host}")
    String hostName;

    @Value("${activate.mail.message}")
    String message;

    @Value("${activate.mail.message.footer}")
    String messageFooter;

    private final String WELCOME_EMAIL_SUBJECT = "Welcome! confirm your email";

    @Override
    public SessionResponse signup(final UserSignUpDto userInfo) throws ToptalException {
        final User newUser = modelMapper.map(userInfo, User.class);
        newUser.setRole(Role.ROLE_USER);
        final User createdUser = userRepository.save(newUser);
        final SessionResponse resp = new SessionResponse();
        if (null != createdUser.getId()) {
            resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
            resp.setOperationMessage("User signed up successflly");
            final UserDto sessionItem = modelMapper.map(createdUser, UserDto.class);
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


    @Override
    public User activateAccount(final String activateKey) throws ToptalException {
        final ActivateKey activateKeyObj = activateKeyRepository.findByKeySerial(activateKey);
        if (activateKeyObj == null) {
            throw ToptalError.USER_ACTIVATION_KEY_ERROR.buildException();
        }
        final User user = userRepository.findOne(activateKeyObj.getUser().getId());
        user.setActivated(true);
        return userRepository.save(user);
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
