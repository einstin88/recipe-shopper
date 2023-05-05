package com.recipeshopper.jwtauthserver.model;

public record Token(
    byte[] pubkey,
    byte[] privkey,
    String token
) {
    
}
