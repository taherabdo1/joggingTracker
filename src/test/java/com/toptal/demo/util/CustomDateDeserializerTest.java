package com.toptal.demo.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

@RunWith(MockitoJUnitRunner.class)
public class CustomDateDeserializerTest {

    final String dateJsonString = "2017-02-05 14:30:00";

    final String invalidDate = "bad Format";

    @Rule
    public ExpectedException thrown = none();

    @Mock
    JsonParser jsonParser;

    @Mock
    DeserializationContext deserializationContext;

    @Test
    public void testDeserialize() throws UnsupportedOperationException, IOException {
        doReturn(dateJsonString).when(jsonParser).getText();
        final CustomDateDeserializer customDateDeserializer = new CustomDateDeserializer();
        final Date date = customDateDeserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(date);
    }

    @Test
    public void testDeserializeInvalidDate() throws UnsupportedOperationException, IOException {
        thrown.expect(RuntimeException.class);
        doReturn(invalidDate).when(jsonParser).getText();
        final CustomDateDeserializer customDateDeserializer = new CustomDateDeserializer();
        customDateDeserializer.deserialize(jsonParser, deserializationContext);
    }
}
