package com.toptal.demo.service;

import com.toptal.demo.controllers.error.ToptalException;

public interface EmailService {

    void send(final String to, final String subjsct, final String text) throws ToptalException;
}
