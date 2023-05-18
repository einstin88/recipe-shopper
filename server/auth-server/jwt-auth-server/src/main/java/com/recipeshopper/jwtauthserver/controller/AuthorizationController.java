package com.recipeshopper.jwtauthserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipeshopper.jwtauthserver.exception.NoTokenFoundException;
import com.recipeshopper.jwtauthserver.repository.TokenRepo;

import lombok.extern.slf4j.Slf4j;

import static com.recipeshopper.jwtauthserver.Utils.Urls.*;

import java.util.Map;

/**
 * This controller is to be accessed from the back-end server only
 */
@RestController
@RequestMapping(path = URL_PREFIX_AUTH)
@Slf4j
public class AuthorizationController {
    @Autowired
    private TokenRepo repo;

    /**
     * Endpoint for retrieving user's public key
     * 
     * @param username
     */
    @GetMapping("/key-uri/{username}")
    public ResponseEntity<Map<String, Object>> getUserPublicKey(
            @PathVariable String username) {

        log.info(">>> Get key for user: {}", username);

        return ResponseEntity.ok(repo.findToken(username)
                .orElseThrow(() -> {
                    throw new NoTokenFoundException(
                            "'%s' is not logged in...".formatted(username));
                })
                .key());
    }

    /**
     * Test point for JWT authentication
     */
    @GetMapping("/authenticate")
    public ResponseEntity<String> testOAuth() {

        return ResponseEntity
                .ok("Oauth working...");
    }
}
