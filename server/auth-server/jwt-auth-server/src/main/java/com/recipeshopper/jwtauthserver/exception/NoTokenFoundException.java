package com.recipeshopper.jwtauthserver.exception;

public class NoTokenFoundException extends RuntimeException {
    public NoTokenFoundException(String msg) {
        super(msg);
    }
}
