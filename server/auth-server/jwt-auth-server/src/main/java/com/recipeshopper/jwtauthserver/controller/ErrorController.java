package com.recipeshopper.jwtauthserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recipeshopper.jwtauthserver.exception.AppUserCreationException;
import com.recipeshopper.jwtauthserver.exception.NoTokenFoundException;
import com.recipeshopper.jwtauthserver.exception.TokenTransactionException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler({ AppUserCreationException.class })
    public ResponseEntity<String> handleUserRegistrationErrors(RuntimeException err) {
        return ResponseEntity
                .badRequest()
                .body(err.getMessage());
    }

    @ExceptionHandler({ TokenTransactionException.class })
    public ResponseEntity<String> handleTokenErrors(RuntimeException err) {
        return ResponseEntity
                .internalServerError()
                .body(err.getMessage());
    }

    @ExceptionHandler({ NoTokenFoundException.class })
    public ResponseEntity<String> handleAuthorizationsErrors(RuntimeException err) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(err.getMessage());
    }
}
