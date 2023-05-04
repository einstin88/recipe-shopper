package com.recipeshopper.jwtauthserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recipeshopper.jwtauthserver.Utils.Urls;
import com.recipeshopper.jwtauthserver.Utils.Utils;
import com.recipeshopper.jwtauthserver.model.AppUser;
import com.recipeshopper.jwtauthserver.service.AppUserAuthService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = Urls.URL_PREFIX_AUTH)
@Slf4j
public class AuthController {
    @Autowired
    private AppUserAuthService svc;

    @GetMapping(Urls.URL_HEALTH)
    public ResponseEntity<String> statusCheck() {
        log.info(">>> Checking server status...");

        return ResponseEntity
                .ok("Server is up");
    }

    @PostMapping(Urls.URL_REGISTER)
    public ResponseEntity<String> postNewUser(
            @RequestParam AppUser newUser,
            @RequestParam List<String> authorities) {

        AppUser populatedUser = Utils.createNewUser(newUser, authorities);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Utils.createTokenResponse(
                        svc.registerNewUser(populatedUser).serialize()));
    }

    @PostMapping(Urls.URL_SIGN_IN_DEFAULT)
    public ResponseEntity<Void> defaultUserSignIn(
            @RequestParam String username,
            @RequestParam String password) {

        return ResponseEntity
                .ok()
                .build();
    }

    /**
     * For experimentation purpose: Http Basic Authentication
     */
    @PostMapping(Urls.URL_SIGN_IN_BASIC)
    public ResponseEntity<String> basicUserSignIn() {
        return ResponseEntity.ok("Basic Login working");
    }
}
