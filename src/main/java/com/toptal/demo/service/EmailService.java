package com.toptal.demo.service;


public interface EmailService {

    void send(final String to, final String subjsct, final String text);
}
