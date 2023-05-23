package com.main.backend.recipeshopper.config.google;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.google.api.services.gmail.GmailScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.main.backend.recipeshopper.exceptions.GoogleCredentialsException;
import com.main.backend.recipeshopper.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class GoogleConfig {

    @Bean
    @Profile("!dev")
    public GoogleCredentials getCloudCredentials() {
        // Load credentials from Google Cloud environment (not using end user's identity
        // via Google SDK)
        // https://cloud.google.com/docs/authentication/application-default-credentials
        // https://cloud.google.com/java/docs/reference/google-auth-library/latest/com.google.auth.oauth2.GoogleCredentials#com_google_auth_oauth2_GoogleCredentials_getApplicationDefault__
        try {
            return GoogleCredentials
                    .getApplicationDefault()
                    .createScoped(GmailScopes.GMAIL_SEND)
                    .createDelegated("recipee-cart@recipee-shopping.com");

        } catch (IOException e) {
            log.error("--- Error reading credentials: {}", e.getMessage());
            throw Utils.generateServerError(
                    "--- Credentials Error: %s",
                    GoogleCredentialsException.class,
                    e.getMessage());
        }
    }

    @Bean
    @Profile("dev")
    public GoogleCredentials getLocalCredentials() {
        try {
            // Load credentials from file (used for local development)
            // https://developers.google.com/identity/protocols/oauth2/service-account#authorizingrequests
            return GoogleCredentials
                    .getApplicationDefault()
                    .createScoped(GmailScopes.GMAIL_SEND)
                    .createDelegated("recipee-cart@recipee-shopping.com");

        } catch (IOException e) {
            log.error("--- Error reading credentials: {}", e.getMessage());
            throw Utils.generateServerError(
                    "--- Credentials Error: %s",
                    GoogleCredentialsException.class,
                    e.getMessage());
        }
    }
}
