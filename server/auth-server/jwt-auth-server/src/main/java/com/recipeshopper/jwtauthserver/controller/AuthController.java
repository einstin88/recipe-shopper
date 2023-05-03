package com.recipeshopper.jwtauthserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipeshopper.jwtauthserver.Urls;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = Urls.URL_PREFIX)
@Slf4j
public class AuthController {

    @GetMapping(Urls.URL_HEALTH)
    public ResponseEntity<String> statusCheck() {
        log.info(">>> Checking server status...");

        return ResponseEntity
                .ok("Server is up");
    }

    @PostMapping(Urls.URL_SIGN_IN_BASIC)
    public ResponseEntity<String> basicUserSignIn(
    ) {

        return ResponseEntity.ok("Basic Login working");
    }
}
