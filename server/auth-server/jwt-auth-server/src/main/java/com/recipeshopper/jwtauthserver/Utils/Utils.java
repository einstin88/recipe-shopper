package com.recipeshopper.jwtauthserver.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.recipeshopper.jwtauthserver.model.AppUser;
import com.recipeshopper.jwtauthserver.model.SCOPES;

import jakarta.json.Json;

public class Utils {
    private static final PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    public static AppUser createNewUser(AppUser user) {

        String password = pwEncoder.encode(user.getPassword());
        
        user.setId("ID_" + UUID.randomUUID().toString().substring(0, 7));
        user.setPassword(password);
        user.setAuthorities(new LinkedList<SimpleGrantedAuthority>(
            List.of(new SimpleGrantedAuthority(SCOPES.READ.toString()))));

        return user;
    }

    public static String createTokenResponse(String token) {
        return Json.createObjectBuilder()
                .add("token", token)
                .build().toString();
    }
}
