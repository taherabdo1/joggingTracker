package com.toptal.demo.controllers.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ToptalError {

    /** The Jogging overlaps another one. */
    JOGGING_OVERLAPING(HttpStatus.CONFLICT, "jogging overlaps with an already added one"),

    /** the jogging is not found*/
    JOGGING_NOT_FOUND(HttpStatus.NOT_FOUND, "there is no Jogging with such ID, maybe you deleted it before"),

    /** the city is not found*/
    CITY_NOT_FOUND(HttpStatus.NOT_FOUND, "this city is not found, we can't get the weather for it"),

    /** the user is not found*/
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER with this email is not found in our system"),

    /** the user is not blocked*/
    USER_NOT_BLOCKED(HttpStatus.NOT_FOUND, "this user is not blocked"),

    /** BAD Request*/
    JOGGING_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "there is validation error, please re-check the request body you sent"),

    /** page size less than or equal zero*/
    JOGGING_VALIDATION_ERROR_PAGE_SIZE(HttpStatus.BAD_REQUEST, "page size must be more than 0"),

    /** page size less than or equal zero*/
    JOGGING_VALIDATION_ERROR_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "page number must be more than or equal 0"),

    /** filter String is not balanced */
    JOGGING_VALIDATION_ERROR_NOT_BALANCED_FILTER_STRING(HttpStatus.BAD_REQUEST,"filter string (parenthesis) is not balanced"),

    /** User doesn't have Jogs */
    USER_DOES_NOT_HAVE_JOGS(HttpStatus.NO_CONTENT,"user doesn't have jogs till now"),

    /** filter String is not balanced */
    JOGGING_VALIDATION_ERROR_NOT_BAD_REPORT_REQUEST_DATA(HttpStatus.BAD_REQUEST, "bad data for generating a report"),

    /** the user doesn't have jogs while he/she want the max ran diatance date*/
    JOGGING_NO_JOGS_FOR_CURRENT_USER(HttpStatus.NO_CONTENT, "Current user doesn'h have jofgs till now"),

    /** the sent Activation Key is not correct */
    USER_ACTIVATION_KEY_ERROR(HttpStatus.BAD_REQUEST, "the sent Activation Key is not correct"),

    /** the sent Activation Key is not correct */
    REPORT_DATES_ERROR_END_DATE_CANT_BE_BEFORE_START_DATE(HttpStatus.BAD_REQUEST, "start date should be before end date"),

    /** incorrect filter criteria */
    INCORRECT_FILTER_CRITERIA(HttpStatus.BAD_REQUEST, "incorrect filter criteria, please recheck"),
    
    /** incorrect filter criteria */
    EXTERNAL_SERVICE_ERROR(HttpStatus.NOT_FOUND, "can't get the weather info from the external service"),
    
    /** incorrect filter criteria */
    MAIL_SERVICE_ERROR(HttpStatus.BAD_REQUEST, "can't send the confirmation mail to the user, check the email again");
        
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
