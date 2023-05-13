package com.main.backend.recipeshopper.config.google;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.api.services.gmail.GmailScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.main.backend.recipeshopper.exceptions.GoogleCredentialsException;
import com.main.backend.recipeshopper.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class GoogleConfig {

    @Value("${GOOGLE_CREDENTIALS}")
    private String credentialsFile;

    @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
    private String gCloudCredentials;

    @Bean
    public GoogleCredentials getCredentials() {
        // log.info("credentialsFile: {}", credentialsFile);
        try {
            if (!gCloudCredentials.isBlank()) {
                // log.info("credentials ran");
                return GoogleCredentials
                        .getApplicationDefault()
                        .createScoped(GmailScopes.GMAIL_SEND);
            }

            // log.info("this ran");
            InputStream is = new ClassPathResource(credentialsFile).getInputStream();

            return GoogleCredentials
                    .fromStream(is)
                    .createScoped(GmailScopes.GMAIL_SEND)
                    .createDelegated("pelie.888888@recipee-shopping.com");

        } catch (IOException e) {
            log.info(credentialsFile, e);
            throw Utils.generateServerError(
                    "--- Credentials Error: %s",
                    GoogleCredentialsException.class,
                    e.getMessage());
        }
    }
}
