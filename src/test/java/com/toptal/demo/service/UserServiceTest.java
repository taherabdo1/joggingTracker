package com.toptal.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UpdateUserDto;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.entities.LoginAttempt;
import com.toptal.demo.entities.User;
import com.toptal.demo.entities.enums.Role;
import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    LoginAttemptRepository loginAttemptRepository;

    @Mock
    ModelMapper modelMapper;

    @Rule
    public ExpectedException thrown = none();

    final Long USER_ID = 1L;

    final String USER_EMAIL = "test@demo.com";

    final Date DATE = new Date();

    final LoginAttempt loginAttempt = new LoginAttempt(1L, DATE, null, 0);

    final UserDto userDto = new UserDto(1L, "token", "cairo", true, false, "test@demo.com", Role.ROLE_USER, 25, "test test test");
    final User user = new User(1L, "test@demo.com", "password", 25, "user full name", Role.ROLE_USER, false, true, "cairo", null, null, loginAttempt);

    final User reactivatedUser = new User(1L, "test@demo.com", "password", 25, "user full name", Role.ROLE_USER, false, false, "cairo", null, null,
            loginAttempt);

    final UpdateUserDto updateUserDto = new UpdateUserDto(1L, "cairo", true, false, "updatedTest@demo.com", Role.ROLE_USER);

    final User updatedUser = new User(1L, "updatedTest@demo.com", "password", 25, "user full name", Role.ROLE_USER, false, true, "cairo", null, null,
            loginAttempt);

    final UserDto updatedUserDto = new UserDto(1L, "token", "cairo", true, false, "updatedTest@demo.com", Role.ROLE_USER, 25, "test test test");

    @Test
    public void testReactivate() {
        doReturn(user).when(userRepository).findOne(USER_ID);
        doReturn(reactivatedUser).when(userRepository).save(user);

        doReturn(userDto).when(modelMapper).map(reactivatedUser, UserDto.class);
        final UserDto result = userService.reactivate(1L);
        assertEquals(result.isBlocked(), false);
    }

    @Test
    public void testGetAll() throws ToptalException {
        final Page<User> users = usersFromDB();
        final Pageable pageRequest = createPageRequest(10, 0);
        doReturn(users).when(userRepository).findAll(null, pageRequest);
        doReturn(userDto).when(modelMapper).map(user, UserDto.class);
        final List<UserDto> resultList = userService.getAll(10, 0, "");

        assertEquals(resultList.get(0).getEmail(), users.getContent().get(0).getEmail());
        assertNotEquals(resultList, 0);

    }

    @Test
    public void testGetAllWithValidCriteria() throws ToptalException {
        final Page<User> users = usersFromDB();
        createPageRequest(10, 0);
        doReturn(users).when(userRepository).findAll(any(Specification.class), any(Pageable.class));
        doReturn(userDto).when(modelMapper).map(user, UserDto.class);
        final List<UserDto> resultList = userService.getAll(10, 0, "(id eq 1)");

        assertEquals(resultList.get(0).getId(), user.getId());
        assertNotEquals(resultList, 0);

    }

    @Test
    public void testGetUserByemail() throws ToptalException {
        doReturn(Optional.of(user)).when(userRepository).findOneByEmail(USER_EMAIL);
        doReturn(userDto).when(modelMapper).map(user, UserDto.class);
        final UserDto result = userService.getUserByemail(USER_EMAIL);

        assertEquals(result.getId(), USER_ID);

    }

    @Test
    public void testGetUserByID() throws ToptalException {
        doReturn(user).when(userRepository).findOne(USER_ID);
        doReturn(userDto).when(modelMapper).map(user, UserDto.class);
        final UserDto result = userService.getUserByID(USER_ID);

        assertEquals(result.getEmail(), USER_EMAIL);

    }

    @Test
    public void testGetUserByIDWithUSER_NOT_FOUNDException() throws ToptalException {
        thrown.expectMessage(ToptalError.USER_NOT_FOUND.getDescription());

        doReturn(null).when(userRepository).findOne(USER_ID);
        doReturn(userDto).when(modelMapper).map(user, UserDto.class);
        userService.getUserByID(USER_ID);

    }

    @Test
    public void testDeleteUser() throws ToptalException {
        doReturn(user).when(userRepository).findOne(USER_ID);
        final Boolean result = userService.delete(USER_ID);

        assertEquals(result, true);

    }

    @Test
    public void testDeleteUserWithUSER_NOT_FOUNDException() throws ToptalException {
        thrown.expectMessage(ToptalError.USER_NOT_FOUND.getDescription());

        doReturn(null).when(userRepository).findOne(USER_ID);
        userService.delete(USER_ID);

    }

    @Test
    public void testGetAllWithINCORRECT_FILTER_CRITERIA() throws ToptalException {
        thrown.expectMessage(ToptalError.INCORRECT_FILTER_CRITERIA.getDescription());

        final Page<User> users = usersFromDB();
        createPageRequest(10, 0);
        doReturn(users).when(userRepository).findAll(any(Specification.class), any(Pageable.class));
        doReturn(userDto).when(modelMapper).map(user, UserDto.class);
        userService.getAll(10, 0, "((id eq 2)");

    }

    @Test
    public void testUpdateUser() throws ToptalException {
        doReturn(user).when(userRepository).findOne(USER_ID);
        doReturn(user).when(modelMapper).map(updateUserDto, User.class);
        doReturn(updatedUser).when(userRepository).save(user);
        doReturn(updatedUserDto).when(modelMapper).map(updatedUser, UserDto.class);
        final UserDto result = userService.update(updateUserDto);

        assertEquals(result.getEmail(), updateUserDto.getEmail());

    }

    private Page<User> usersFromDB() {
        final List<User> userList = new ArrayList<>();
        userList.add(user);
        final PageImpl<User> usersPage = new PageImpl<>(userList, createPageRequest(10, 0), 10);

        return usersPage;
    }

    private Pageable createPageRequest(final int pageSize, final int pageNumber) {
        return new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "id");
    }

}
