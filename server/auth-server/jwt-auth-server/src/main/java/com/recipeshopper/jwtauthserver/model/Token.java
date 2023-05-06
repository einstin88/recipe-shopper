package com.recipeshopper.jwtauthserver.model;

import java.util.Map;

public record Token(
    Map<String, Object> key,
    String token
) {
    
}
