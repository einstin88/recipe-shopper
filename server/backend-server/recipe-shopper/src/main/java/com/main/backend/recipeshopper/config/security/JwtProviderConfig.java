package com.main.backend.recipeshopper.config.security;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import com.main.backend.recipeshopper.service.JwtAuthenticationService;
import com.main.backend.recipeshopper.utils.Utils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTParser;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JwtProviderConfig {

    @Autowired
    private JwtAuthenticationService svc;

    /**
     * For validating JWT by Spring Security
     * 
     * @see <a href=
     *      "https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html"
     *      >Documentation</a>
     */
    @Bean
    public JwtAuthenticationProvider jwtAuthProvider() {
        return new JwtAuthenticationProvider(jwtDecoder());
    }

    /**
     * Worker of AuthProvider: to decode, validate and verify the JWT.
     * This custom implementation is to retrieve Public key from the Auth-server
     * 
     * @see <a href=
     *      "https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html#oauth2resourceserver-jwt-architecture"
     *      >Reference</a>
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                log.debug(">>> Decoder running. token: {}", token);

                try {
                    // Retrieve username from token
                    String username = JWTParser.parse(token)
                            .getJWTClaimsSet().getSubject();

                    log.debug(">>> Decoder running. username: {}", username);

                    // Retrieve user's public key
                    RSAKey userKey = RSAKey.parse(
                            svc.getUserPublicKey(username));

                    // Decode token with the public key
                    JwtDecoder decoder = NimbusJwtDecoder
                            .withPublicKey(userKey.toRSAPublicKey())
                            .signatureAlgorithm(SignatureAlgorithm.RS256)
                            .build();

                    // Return authenticated token to the AuthProvider.authenticate()
                    return decoder.decode(token);

                } catch (ParseException e) {
                    throw Utils.generateServerError(
                            "Token/Key error: %s",
                            AccessDeniedException.class,
                            e.getMessage());
                } catch (JOSEException e) {
                    throw Utils.generateServerError(
                            "Invalid key",
                            AccessDeniedException.class);
                }
            }
        };
    }

}
