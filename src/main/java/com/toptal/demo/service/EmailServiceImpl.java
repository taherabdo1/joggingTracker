package com.toptal.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.toptal.demo.controllers.error.ToptalError;
import com.toptal.demo.controllers.error.ToptalException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void send(final String to, final String subjsct, final String text) throws ToptalException {
        final SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setTo(to);
            message.setSubject(subjsct);
            message.setText(text);
            emailSender.send(message);
        } catch (final Exception e) {
            throw ToptalError.MAIL_SERVICE_ERROR.buildException();
        }

    }
}
