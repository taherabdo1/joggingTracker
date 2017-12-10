package com.toptal.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserDto;
import com.toptal.demo.dto.UserRequestDto;
import com.toptal.demo.entities.User;
import com.toptal.demo.repositories.LoginAttemptRepository;
import com.toptal.demo.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginAttemptRepository loginAttemptRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserDto reactivate(final Long userId) {
        final User userToReactivate = userRepository.findOne(userId);
        userToReactivate.getLoginAttempt().setNumberOfTrials(0);
        userToReactivate.setBlocked(false);
        // re-initialise the login trials for this user
        loginAttemptRepository.save(userToReactivate.getLoginAttempt());
        final User reactivated = userRepository.save(userToReactivate);

        final UserDto userDto = modelMapper.map(reactivated, UserDto.class);

        return userDto;

    }

    @Override
    public List<UserDto> getAll() {
        final List<User> users = Lists.newArrayList(userRepository.findAll());
        final List<UserDto> resultList = new ArrayList<>();
        for (final User user : users) {
            resultList.add(modelMapper.map(user, UserDto.class));
        }
        return resultList;
    }

    @Override
    public UserDto getUserByemail(final String email) throws ToptalException {
        final User user = userRepository.findOneByEmail(email).get();
        if (user == null) {
            throw ToptalError.USER_NOT_FOUND.buildException();
        }
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByID(final long id) throws ToptalException {
        final User user = userRepository.findOne(id);
        if (user == null) {
            throw ToptalError.USER_NOT_FOUND.buildException();
        }
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void delete(final long id) throws ToptalException {
        final User user = userRepository.findOne(id);
        if (user == null) {
            throw ToptalError.USER_NOT_FOUND.buildException();
        }
        userRepository.delete(id);
    }

    @Override
    public UserDto update(final UserRequestDto userDto) throws ToptalException {
        final User userFromDB = userRepository.findOneByEmail(userDto.getEmail()).get();
        if (userFromDB == null) {
            throw ToptalError.USER_NOT_FOUND.buildException();
        }
        User userToSave = modelMapper.map(userDto, User.class);
        if (userToSave.getPassword() == "" || userToSave.getPassword() == null) {
            userToSave.setPassword(userFromDB.getPassword());
        }
        userToSave.setId(userFromDB.getId());
        if (userToSave.getRole() == null) {
            userToSave.setRole(userFromDB.getRole());
        }
        userToSave = userRepository.save(userToSave);
        userToSave.getJoggings();
        return modelMapper.map(userToSave, UserDto.class);
    }
}
