package com.recipeshopper.jwtauthserver.model;

import java.security.KeyPair;

import com.nimbusds.jose.JWSObject;

public record Token(
    KeyPair keys,
    JWSObject token
) {
    
}
