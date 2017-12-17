package com.toptal.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.controllers.filtter.CriteriaParser;
import com.toptal.demo.controllers.filtter.UserSpecification;
import com.toptal.demo.dto.UpdateUserDto;
import com.toptal.demo.dto.UserDto;
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
    public List<UserDto> getAll(final int pageSize, final int pageNumer, final String filterBy) throws ToptalException {
        if (pageSize <= 0) {
            throw ToptalError.JOGGING_VALIDATION_ERROR_PAGE_SIZE.buildException();
        }
        if (pageNumer < 0) {
            throw ToptalError.JOGGING_VALIDATION_ERROR_PAGE_NUMBER.buildException();
        }
        final Pageable pageable = createPageRequest(pageSize, pageNumer);
        List<User> users = null;
        if (filterBy != "" && filterBy != null) {
            try {
                final List<Object> filterObjects = CriteriaParser.parse(filterBy);
                users = Lists.newArrayList(userRepository.findAll(UserSpecification.getJogSpecification(filterObjects), pageable));
            } catch (final Exception e) {
                throw ToptalError.INCORRECT_FILTER_CRITERIA.buildException();
            }

        } else {// no filter criteria needed
            users = Lists.newArrayList(userRepository.findAll(null, pageable));
        }

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
    public UserDto update(final UpdateUserDto updateUserDto) throws ToptalException {
        final User userFromDB = userRepository.findOne(updateUserDto.getId());
        if (userFromDB == null) {
            throw ToptalError.USER_NOT_FOUND.buildException();
        }
        User userToSave = modelMapper.map(updateUserDto, User.class);
        if (userToSave.getPassword() == "" || userToSave.getPassword() == null) {
            userToSave.setPassword(userFromDB.getPassword());
        }
        if (userToSave.getActivated() == null) {
            userToSave.setActivated(userFromDB.getActivated());
        }
        if (userToSave.getAge() == null) {
            userToSave.setAge(userFromDB.getAge());
        }
        if (userToSave.getCity() == null || userToSave.getCity() == "") {
            userToSave.setCity(userFromDB.getCity());
        }
        if (userToSave.getEmail() == null || userToSave.getEmail() == "") {
            userToSave.setEmail(userFromDB.getEmail());
        }
        if (userToSave.getFullName() == null || userToSave.getEmail() == "") {
            userToSave.setFullName(userFromDB.getFullName());
        }
        if (userToSave.getPassword() == null || userToSave.getPassword() == "") {
            userToSave.setPassword(userFromDB.getPassword());
        }
        if (userToSave.getRole() == null) {
            userToSave.setRole(userFromDB.getRole());
        }
        userToSave = userRepository.save(userToSave);
        userToSave.getJoggings();
        return modelMapper.map(userToSave, UserDto.class);
    }

    private Pageable createPageRequest(final int pageSize, final int pageNumber) {
        return new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "id");
    }
}
