package com.toptal.demo.service;

import com.toptal.demo.controllers.error.ToptalException;
import com.toptal.demo.dto.UserSignUpDto;
import com.toptal.demo.entities.User;
import com.toptal.demo.security.response.session.SessionResponse;

public interface AuthenicationService {

    SessionResponse signup(final UserSignUpDto userInfo) throws ToptalException;

    User activateAccount(final String activateKey) throws ToptalException;
}
