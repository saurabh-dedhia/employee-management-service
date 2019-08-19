package com.freedom.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IntegrityConstraintViolationException extends RuntimeException {

    public IntegrityConstraintViolationException() { super();}

    public IntegrityConstraintViolationException(String message) {
        super(message);
    }

    public IntegrityConstraintViolationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
