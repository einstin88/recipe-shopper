package com.recipeshopper.jwtauthserver.model;

public enum SCOPES {
    READ("read"),
    WRITE("write");

    public final String scope;

    private SCOPES(String scope) {
        this.scope = scope;
    }
}
