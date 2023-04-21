package com.main.backend.recipeshopper.exceptions;

public class IncorrectRequestException extends RuntimeException {
    public IncorrectRequestException(String msg) {
        super(msg);
    }
}
