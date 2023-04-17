package com.main.backend.recipeshopper.exceptions;

public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException(String msg) {
        super(msg);
    }
}
