package com.toptal.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.dto.UserRequestDto;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.service.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserController userController;

    @Rule
    public ExpectedException thrown = none();

    final private String email = "user01@test.com";

    final private long ID = 1L;
    @Before
    public void init() {
    }

    @Test
    public void testGetAllUsers() throws ToptalException {
        userController.getAllUsers(10, 0);
        verify(userService).getAll(10, 0);
    }

    @Test
    public void testGetAllUsersThrowToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(userService).getAll(10, 0);
        userController.getAllUsers(10, 0);
    }

    @Test
    public void testGetUserByEmail() throws ToptalException {
        userController.getUserByEmail(email);
        verify(userService).getUserByemail(email);
    }

    @Test
    public void testGetUserByEmailThrowToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(userService).getUserByemail(email);
        userController.getUserByEmail(email);
    }

    @Test
    public void testGetUserByID() throws ToptalException {
        final UserDto user = new UserDto(1L, "token", "cairo", true, false, "user1@test.com", Role.ROLE_USER, 20, "test user full name");
        doReturn(user).when(userService).getUserByID(anyLong());
        final UserDto userDto = userController.getUserByID(ID);
        assertEquals(userDto.getEmail(), user.getEmail());
    }


    @Test
    public void testGetUserByIDThrowToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(userService).getUserByID(ID);
        userController.getUserByID(ID);
    }

    @Test
    public void testDeleteUser() throws ToptalException {
        new UserDto(1L, "token", "cairo", true, false, "user1@test.com", Role.ROLE_USER, 20, "test user full name");
        userController.deleteUserByID(ID);
        verify(userService).delete(ID);
    }

    @Test
    public void testDeleteUserByIDThrowToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(userService).delete(ID);
        userController.deleteUserByID(ID);
    }

    @Test
    public void testUpdateUser() throws ToptalException {
        final UserRequestDto userRequestDto = new UserRequestDto("cairo", true, false, "user1@test.com", Role.ROLE_USER);
        final UserDto userDto = new UserDto(1L, "token", "cairo", true, false, "user1@test.com", Role.ROLE_USER, 20, "test user full name");
        doReturn(userDto).when(userService).update(userRequestDto);
        final UserDto returnedUserDto = userController.updateUser(userRequestDto);
        assertEquals(returnedUserDto.getEmail(), userRequestDto.getEmail());
    }

    @Test
    public void testUpdateUserThrowToptalException() throws ToptalException {
        final UserRequestDto userRequestDto = new UserRequestDto("cairo", true, false, "user1@test.com", Role.ROLE_USER);
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(userService).update(any());
        userController.updateUser(userRequestDto);
    }

    @Test
    public void testReactivateUser() throws ToptalException {
        userController.reactivate(ID);
        verify(userService).reactivate(ID);
    }

    @Test
    public void testReactivateUserThrowToptalException() throws ToptalException {
        thrown.expect(ToptalException.class);
        doThrow(ToptalException.class).when(userService).reactivate(ID);
        userController.reactivate(ID);
    }


    private List<UserDto> getUsersList() {
        final List<UserDto> usersList = new ArrayList<>();
        usersList.add(new UserDto(1L, "token", "cairo", true, false, "user1@test.com", Role.ROLE_USER, 20, "test user full name"));
        usersList.add(new UserDto(2L, "token", "giza", true, false, "user2@test.com", Role.ROLE_MANAGER, 20, "test user full name"));
        usersList.add(new UserDto(3L, "token", "london", true, false, "user3@test.com", Role.ROLE_ADMIN, 20, "test user full name"));
        return usersList;
    }
}
