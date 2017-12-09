package com.toptal.demo.controllers.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ToptalError {

    /** The Jogging overlaps another one. */
    JOGGING_OVERLAPING(HttpStatus.CONFLICT, "jogging overlaps with an already added one"),

    /** the jogging is not found*/
    JOGGING_NOT_FOUND(HttpStatus.NOT_FOUND, "there is no Jogging with such ID, maybe you deleted it before"),

    /** BAD Request*/
    JOGGING_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "there is validation error, please re-check the request body you sent");

    /** The http status. */
    private HttpStatus httpStatus;

    /** The description. */
    private String description;

    private ToptalError(final HttpStatus httpStatus, final String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }
    
    public ToptalException buildException() {
        return new ToptalException(this);
    }


}
