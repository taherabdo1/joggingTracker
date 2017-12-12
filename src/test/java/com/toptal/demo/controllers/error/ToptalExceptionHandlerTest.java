package com.toptal.demo.controllers.error;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class ToptalExceptionHandlerTest {

    @Test
    public void testHandleException() {
        final ToptalExceptionHandler handler = new ToptalExceptionHandler();
        final ToptalException exception = new ToptalException(ToptalError.CITY_NOT_FOUND);
        final ResponseEntity<ToptalErrorReponse> resultEnity = handler.handleException(exception);
        assertNotNull(resultEnity);
        assertEquals(resultEnity.getStatusCode().value(), 404);
    }

}
