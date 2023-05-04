package com.recipeshopper.jwtauthserver.Utils;

import java.util.List;

import com.recipeshopper.jwtauthserver.model.AppUser;

import jakarta.json.Json;

public class Utils {
    public static AppUser createNewUser(AppUser user, List<String> auths) {

        return null;
    }

    public static String createTokenResponse(String token) {
        return Json.createObjectBuilder()
                .add("token", token)
                .build().toString();
    }
}
