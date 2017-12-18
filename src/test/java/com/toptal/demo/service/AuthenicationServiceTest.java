package com.toptal.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.dto.UserSignUpDto;
import com.toptal.demo.entities.ActivateKey;
import com.toptal.demo.entities.User;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.repositories.ActivateKeyRepository;
import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;
import com.toptal.demo.security.response.session.OperationResponse.ResponseStatusEnum;
import com.toptal.demo.security.response.session.SessionResponse;
import com.toptal.demo.util.ToptalConfig;

@RunWith(MockitoJUnitRunner.class)
public class AuthenicationServiceTest {

    @InjectMocks
    AuthenicationServiceImpl authenticationService;

    @Mock
    UserRepository userRepository;

    @Mock
    ActivateKeyRepository activateKeyRepository;

    @Mock
    LoginAttemptRepository loginAttemptRepository;

    @Mock
    EmailServiceImpl emailService;

    @Mock
    ModelMapper modelMapper;

    @Mock
    ToptalConfig toptalConfig;

    @Rule
    public ExpectedException thrown = none();

    final UserSignUpDto userInfo = new UserSignUpDto("test@demo.com", "password", "cairo", 25, "test user full name");

    final UserDto userDto = new UserDto(1L, "token", "cairo", false, false, "test@demo.com", Role.ROLE_USER, 25, "test user full name");

    final User user = new User(1L, "test@demo.com", "password", 25, "user full name", Role.ROLE_USER, false, false, "cairo", null, null, null);

    final String activationSerial = UUID.randomUUID().toString();

    final ActivateKey activateKey = new ActivateKey(1l, activationSerial, user);
    @Test
    public void testSignup() throws ToptalException {
        doReturn(user).when(userRepository).save(any(User.class));
        doReturn(user).when(modelMapper).map(userInfo, User.class);
        doReturn(userDto).when(modelMapper).map(user, UserDto.class);
        doReturn("message").when(toptalConfig).getMessage();
        doReturn("messageFooter").when(toptalConfig).getMessageFooter();
        doReturn("localhost").when(toptalConfig).getHostName();

        doReturn(new ActivateKey(1L, "key", user)).when(activateKeyRepository).save(any(ActivateKey.class));
        final SessionResponse response = authenticationService.signup(userInfo);
        assertNotNull(response);
        assertEquals(response.getItem().getEmail(), userDto.getEmail());
        assertEquals(response.getOperationStatus(), ResponseStatusEnum.SUCCESS);
    }

    @Test
    public void testActivateAccount() throws ToptalException {
        final User activatedUser = user;
        activatedUser.setActivated(true);

        doReturn(activateKey).when(activateKeyRepository).findByKeySerial(activationSerial);
        doReturn(user).when(userRepository).findOne(user.getId());
        doReturn(activatedUser).when(userRepository).save(user);

        final User returned = authenticationService.activateAccount(activationSerial);
        assertNotNull(activatedUser);
        assertEquals(returned.getActivated(), true);
    }

    @Test
    public void testActivateAccountWithUSER_ACTIVATION_KEY_ERRORException() throws ToptalException {
        thrown.expectMessage(ToptalError.USER_ACTIVATION_KEY_ERROR.getDescription());
        authenticationService.activateAccount(activationSerial);
    }

}
