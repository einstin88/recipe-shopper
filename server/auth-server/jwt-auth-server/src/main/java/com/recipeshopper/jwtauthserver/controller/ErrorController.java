package com.recipeshopper.jwtauthserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recipeshopper.jwtauthserver.exception.AppUserCreationException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({ AppUserCreationException.class })
    public ResponseEntity<String> handleError(RuntimeException err) {
        return ResponseEntity
                .badRequest()
                .body(err.getMessage());
    }
}
