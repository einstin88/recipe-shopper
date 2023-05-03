package com.recipeshopper.authserver.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration(proxyBeanMethods = false)
public class AuthServerConfig {

    /**
     * Security chain for Protocol endpoints
     * 
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        return http.build();
    }

    /**
     * Configures the protocol endpoints:
     * (1.) Authorization,
     * (2.) Token,
     * (3.) Token introspection,
     * (4.) Token revocation,
     * (5.) Server metadata
     * (6.) JWK Set
     */
    @Bean
    public AuthorizationServerSettings authServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * Repo for managing authenticated clients
     * 
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate template) {
        RegisteredClient client = RegisteredClient
                // Unique ID
                .withId(UUID.randomUUID().toString())
                // Client/App identifier
                .clientId("shopper-client")
                // https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-dpe
                .clientSecret("{noop}secret")
                .clientAuthenticationMethods(methods -> {
                    // https://developer.okta.com/docs/reference/api/oidc/#client-authentication-methods
                    methods.add(ClientAuthenticationMethod.CLIENT_SECRET_JWT);
                })
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.addAll(List.of(
                            AuthorizationGrantType.AUTHORIZATION_CODE,
                            AuthorizationGrantType.JWT_BEARER,
                            AuthorizationGrantType.REFRESH_TOKEN));
                })
                .redirectUri("http://localhost:8080/login/oauth2/code/shopper-client-oidc")
                .redirectUri("http://localhost:8080/authorized")
                .scope(OidcScopes.OPENID)
                .build();

        RegisteredClientRepository clientRepo = new JdbcRegisteredClientRepository(template);
        // clientRepo.save(client);
        return clientRepo;
    }

    /**
     * For signing access tokens
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * Generate public & private keys
     */
    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
