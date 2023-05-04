package com.recipeshopper.jwtauthserver.exception;

public class AppUserCreationException extends RuntimeException {
    public AppUserCreationException(String msg) {
        super(msg);
    }
}
