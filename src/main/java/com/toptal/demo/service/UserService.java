package com.toptal.demo.service;

import java.util.List;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.dto.UserRequestDto;

public interface UserService {

    UserDto reactivate(final Long userId);

    UserDto getUserByemail(String email) throws ToptalException;

    UserDto getUserByID(long id) throws ToptalException;

    void delete(long id) throws ToptalException;

    UserDto update(UserRequestDto userDto) throws ToptalException;

    List<UserDto> getAll();
}
