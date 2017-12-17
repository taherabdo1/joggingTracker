package com.toptal.demo.controllers.error;

import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ToptalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle exception.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(ToptalException.class)
    public final ResponseEntity<ToptalErrorReponse> handleException(final ToptalException exception) {
        final ToptalError toptalError = exception.getToptalError();
        final ToptalErrorReponse errorResponse = ToptalErrorReponse.builder().description(toptalError.getDescription()).error(toptalError.name())
                .status(toptalError.getHttpStatus().name()).build();
        return new ResponseEntity<ToptalErrorReponse>(errorResponse, toptalError.getHttpStatus());
    }



    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public final ResponseEntity<ToptalErrorReponse> handleValidationException(final MethodArgumentNotValidException
    // exception) {
    // final ToptalErrorReponse errorResponse =
    // ToptalErrorReponse.builder().description(exception.getMessage()).error("Validation Error")
    // .status("BAD REQUEST").build();
    // return new ResponseEntity<ToptalErrorReponse>(errorResponse, HttpStatus.BAD_REQUEST);
    // }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), status);
        // return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }

    private ResponseEntity<Object> buildErrorResponse(final Exception ex, final String error, final HttpStatus status) {

        return new ResponseEntity<Object>(error, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        new ArrayList<>();

        // final ToptalException apiError = new ToptalException(ToptalError.JOGGING_VALIDATION_ERROR);
        // return handleExceptionInternal(ex, apiError, headers, apiError.getToptalError().getHttpStatus(), request);
        return buildErrorResponse(ex, ex.getMessage(), status);

    }


}
