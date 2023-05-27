package com.recipeshopper.jwtauthserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recipeshopper.jwtauthserver.Utils.Utils;
import com.recipeshopper.jwtauthserver.model.AppUser;
import com.recipeshopper.jwtauthserver.service.AppUserAuthenticationService;

import static com.recipeshopper.jwtauthserver.Utils.Urls.*;

import lombok.extern.slf4j.Slf4j;

/**
 * This controller is to be accessed from front-end
 */
@RestController
@RequestMapping(path = URL_PREFIX_AUTH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AuthenticationController {
        @Autowired
        private AppUserAuthenticationService svc;

        @GetMapping(URL_HEALTH)
        public ResponseEntity<String> statusCheck() {
                log.debug(">>> Checking server status...");

                return ResponseEntity
                                .ok("Server is up");
        }

        /**
         * EP for new user registration
         * 
         * @param newUser JSON format
         * @return JWS of the new user
         */
        @PostMapping(path = URL_REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> postNewUser(
                        @RequestBody AppUser newUser) {

                log.debug(">>> Request to register new user: {}", newUser);
                AppUser populatedUser = Utils.createNewUser(newUser);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(Utils.createTokenResponse(
                                                svc.registerNewUser(populatedUser)));
        }

        /**
         * The entry point upon successful authentication.
         * 
         * @param user
         * @return compact JWS
         */
        @PostMapping(URL_AUTHENTICATED)
        public ResponseEntity<String> authenticatedCallback(
                        @AuthenticationPrincipal AppUser user) {

                String username = user.getUsername();
                log.debug(">>> Succesfully authenticated {}", username);

                return ResponseEntity.ok(
                                Utils.createTokenResponse(
                                                svc.processAuthenticatedUser(username, user.getEmail())));
        }

        @GetMapping(URL_SIGN_IN_DEFAULT)
        public ResponseEntity<String> authenticationHandler(
                        @RequestParam(required = false) String error) {

                if (error != null) {
                        log.debug(">>> Log in failed");
                        return ResponseEntity
                                        .badRequest()
                                        .body("Wrong username/password");
                }

                // No log-in page. This error status should be handled by front-end/load balancer
                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .build();
        }

        @GetMapping(URL_LOG_OUT + "/{username}")
        public ResponseEntity<Void> signoutUser(@PathVariable String username) {

                log.debug("Logging '{}' out...", username);

                svc.removeToken(username);

                return ResponseEntity
                                .ok()
                                .build();
        }

        /**
         * For experimentation purpose: Http Basic Authentication
         */
        @PostMapping(URL_SIGN_IN_BASIC)
        public ResponseEntity<String> basicUserSignIn() {
                return ResponseEntity.ok("Basic Login working");
        }
}
