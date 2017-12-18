package com.toptal.demo.service;

import java.util.List;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UpdateUserDto;
import com.toptal.demo.dto.UserDto;

public interface UserService {

    UserDto reactivate(final Long userId) throws ToptalException;

    UserDto getUserByemail(String email) throws ToptalException;

    UserDto getUserByID(long id) throws ToptalException;

    Boolean delete(long id) throws ToptalException;

    UserDto update(UpdateUserDto updateUserDto) throws ToptalException;

    List<UserDto> getAll(final int pageSize, final int pageNumber, String filterBy) throws ToptalException;
}
