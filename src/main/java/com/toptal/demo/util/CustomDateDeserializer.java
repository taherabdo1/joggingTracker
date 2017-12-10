package com.toptal.demo.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(final JsonParser jsonparser, final DeserializationContext deserializationcontext) throws IOException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String date = jsonparser.getText();
        try {
            return format.parse(date);
        } catch (final ParseException e) {
            throw new RuntimeException("there is validation error, please re-check the request body you sent");
        }
    }
}
