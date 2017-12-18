package com.toptal.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.dto.UserSignUpDto;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.security.response.session.OperationResponse.ResponseStatusEnum;
import com.toptal.demo.security.response.session.SessionResponse;
import com.toptal.demo.service.AuthenicationService;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {

    @Mock
    AuthenicationService authenticationService;

    @InjectMocks
    AuthenticationController authenticationController;

    @Rule
    public ExpectedException thrown = none();

    final static String ACTIVATION_KEY = "any activation key";

    final UserSignUpDto userInfo = new UserSignUpDto("test@demo.com", "password", "cairo", 25, "test user full name");

    final UserDto userDto = new UserDto(1L, "token", "cairo", false, false, "test@demo.com", Role.ROLE_USER, 25, "test user full name");

    @Test
    public void testSignup() throws ToptalException {
        final SessionResponse response = new SessionResponse();
        response.setOperationStatus(ResponseStatusEnum.SUCCESS);
        response.setItem(userDto);
        response.setOperationMessage("operation succedded");
        doReturn(response).when(authenticationService).signup(userInfo);
        assertEquals(authenticationController.signup(userInfo).getItem().getEmail(), userInfo.getEmail());
    }

    @Test
    public void testSignupWithToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(authenticationService).signup(userInfo);
        authenticationController.signup(userInfo);
    }

    @Test
    public void testActivateAccount() throws ToptalException {
        authenticationController.activateAccount(ACTIVATION_KEY);
        verify(authenticationService).activateAccount(ACTIVATION_KEY);
    }

    @Test
    public void testActivateAccountWithThrowToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(authenticationService).activateAccount(ACTIVATION_KEY);
        authenticationController.activateAccount(ACTIVATION_KEY);
    }
}
