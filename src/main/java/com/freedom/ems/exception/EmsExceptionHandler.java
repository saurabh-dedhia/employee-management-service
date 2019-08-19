package com.freedom.ems.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class EmsExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmsExceptionHandler.class.getName());
    private static final String EMPTY = "";

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Object> handleEmployeeNotFound(EmployeeNotFoundException exception,
                                                         WebRequest webRequest) {

        LOGGER.error(EMPTY, exception);

        CustomResponse customResponse = new CustomResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), "Employee Not Found");

        return handleExceptionInternal(exception, customResponse, null, HttpStatus.NOT_FOUND, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders httpHeaders, HttpStatus status,
                                                                  WebRequest webRequest) {

        LOGGER.error(EMPTY, exception);

        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ex -> ex.getField() + " " + ex.getDefaultMessage())
                .collect(Collectors.toList());

        CustomResponse customResponse = new CustomResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), errors.toString());

        return handleExceptionInternal(exception, customResponse, httpHeaders, HttpStatus.BAD_REQUEST,
                webRequest);
    }
}
