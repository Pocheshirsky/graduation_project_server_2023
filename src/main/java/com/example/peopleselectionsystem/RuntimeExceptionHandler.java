package com.example.peopleselectionsystem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RuntimeExceptionHandler {
    record Message(String message) { }

    @ExceptionHandler
    public ResponseEntity<?> handleException(RuntimeException ex) {
        return new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
