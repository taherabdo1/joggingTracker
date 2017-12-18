package com.toptal.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ToptalConfig {


    @Value("${weather.api.rest.uri}")
    String weatherApiURI;

    @Value("${weather.api.rest.result.mode}")
    String resultMode;

    @Value("${weather.api.rest.token}")
    String apiToken;
    
    @Value("${running.host}")
    String hostName;

    @Value("${activate.mail.message}")
    String message;

    @Value("${activate.mail.message.footer}")
    String messageFooter;


}
