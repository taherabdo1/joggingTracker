package com.toptal.demo.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDateDaySerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(final Date date, final JsonGenerator jGen, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        jGen.writeString(format.format(date));
    }

}
