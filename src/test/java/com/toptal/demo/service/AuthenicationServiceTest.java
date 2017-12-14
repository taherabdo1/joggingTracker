package com.toptal.demo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.dto.UserSignUpDto;
import com.toptal.demo.entities.User;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.repositories.ActivateKeyRepository;
import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;

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

    final UserSignUpDto userInfo = new UserSignUpDto("test@demo.com", "password", "cairo", 25, "test user full name");

    final UserDto userDto = new UserDto(1L, "token", "cairo", false, false, "test@demo.com", Role.ROLE_USER, 25, "test user full name");

    final User user = new User(1L, "test@demo.com", "password", 25, "user full name", Role.ROLE_USER, false, false, "cairo", null, null, null);

    @Test
    public void testSignup() throws ToptalException {
        // doReturn(user).when(userRepository).save(any(User.class));
        // doReturn(new ActivateKey(1L, "key", user)).when(activateKeyRepository).save(any(ActivateKey.class));
        // // doReturn(user).when(modelMapper).map(any(UserSignUpDto.class), any(User.class));
        // authenticationService.signup(userInfo);

    }
}
