package com.gotta_watch_them_all.app.work.infrastructure.entrypoint.exception;

import com.gotta_watch_them_all.app.work.core.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class WorkExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AnySearchValueFoundException.class)
    public ResponseEntity<String> on(AnySearchValueFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalImdbIdGivenException.class)
    public ResponseEntity<String> on(IllegalImdbIdGivenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalTitleGivenException.class)
    public ResponseEntity<String> on(IllegalTitleGivenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadHttpRequestException.class)
    public ResponseEntity<String> on(BadHttpRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TooManySearchArgumentsException.class)
    public ResponseEntity<String> on(TooManySearchArgumentsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
