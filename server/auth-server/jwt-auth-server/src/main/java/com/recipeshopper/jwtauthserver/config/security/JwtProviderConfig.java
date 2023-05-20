package com.recipeshopper.jwtauthserver.config.security;

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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTParser;
import com.recipeshopper.jwtauthserver.exception.TokenTransactionException;
import com.recipeshopper.jwtauthserver.repository.TokenRepo;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JwtProviderConfig {

    @Autowired
    private TokenRepo tokenRepo;

    /**
     * For validating authentication with JWT
     * 
     * @see <a href=
     *      "https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html">Documentation</a>
     */
    @Bean
    public JwtAuthenticationProvider jwtAuthProvider() {
        return new JwtAuthenticationProvider(jwtDecoder());
    }

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
                            tokenRepo.findToken(username)
                                    .orElseThrow(() -> {
                                        throw new AccessDeniedException(
                                                "Token expired for user: " + username);
                                    })
                                    .key());
                    
                    // Decode token with the public key
                    JwtDecoder decoder = NimbusJwtDecoder
                            .withPublicKey(userKey.toRSAPublicKey())
                            .signatureAlgorithm(SignatureAlgorithm.RS256)
                            .build();

                    // Return token to the AuthProvider for authentication
                    return decoder.decode(token);

                } catch (ParseException e) {
                    throw new TokenTransactionException(
                            "Token key error for user");
                } catch (JOSEException e) {
                    throw new TokenTransactionException(
                            "Token key parameters error");
                }
            }
        };
    }

}
