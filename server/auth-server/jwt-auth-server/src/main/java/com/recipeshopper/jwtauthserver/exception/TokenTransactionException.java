package com.recipeshopper.jwtauthserver.exception;

public class TokenTransactionException extends RuntimeException {
    public TokenTransactionException(String msg) {
        super(msg);
    }
}
