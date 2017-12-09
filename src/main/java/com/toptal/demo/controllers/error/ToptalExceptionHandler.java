package com.toptal.demo.controllers.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
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

    // @Override
    // protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final
    // HttpHeaders headers, final HttpStatus status,
    // final WebRequest request) {
    // final List<String> errors = new ArrayList<>();
    // for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
    // errors.add(error.getField() + ": " + error.getDefaultMessage());
    // }
    // for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
    // errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    // }
    //
    // final ToptalException apiError = new ToptalException(ToptalError.JOGGING_VALIDATION_ERROR);
    // return handleExceptionInternal(ex, apiError, headers, apiError.getToptalError().getHttpStatus(), request);
    // }

    // @ExceptionHandler(BindException.class)
    // protected ResponseEntity<Object> handleBindException(final BindException ex) {
    // final ToptalError toptalError = ToptalError.JOGGING_VALIDATION_ERROR;
    // final ToptalErrorReponse errorResponse =
    // ToptalErrorReponse.builder().description(toptalError.getDescription()).error(toptalError.name())
    // .status(toptalError.getHttpStatus().name()).build();
    // return new ResponseEntity<Object>(errorResponse, toptalError.getHttpStatus());
    // }

}
