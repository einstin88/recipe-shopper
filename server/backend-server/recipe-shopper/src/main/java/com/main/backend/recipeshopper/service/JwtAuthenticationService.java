package com.main.backend.recipeshopper.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.main.backend.recipeshopper.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * A service mainly to retrieve JWS's public key
 */
@Service
@Slf4j
public class JwtAuthenticationService {
    @Value("${KEY_URL}")
    private String keyUrl;

    public String getUserPublicKey(String username) {
        RestTemplate client = new RestTemplate();

        URI url = UriComponentsBuilder
                .fromHttpUrl(keyUrl)
                .pathSegment(username)
                .build().toUri();

        RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        try {
            ResponseEntity<String> response = client.exchange(request, String.class);

            String pubKey = response.getBody();
            log.debug("pubKey: {}", pubKey);

            return pubKey;

        } catch (RestClientException e) {
            throw Utils.generateServerError(
                    "Failed to get key for user: %s",
                    AccessDeniedException.class,
                    username);
        }

    }

}
