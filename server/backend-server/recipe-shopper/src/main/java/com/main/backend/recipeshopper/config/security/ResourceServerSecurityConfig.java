package com.main.backend.recipeshopper.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;

import static com.main.backend.recipeshopper.utils.Urls.*;

/**
 * Spring Security Configurations - mainly to
 * (1.) define which endpoints are protected/ publicly accessible,
 * (2.) protect endpoints (as oauth2 resource server) by validating JWS with
 * public key
 * 
 * User Identity authentication is handled by a separate Auth-Server and is the
 * source of
 * truth used by the OAuth resource server to validate JWT claims
 */
@Configuration
@EnableWebSecurity // Denotes class for Spring to import and look for security settings
@EnableMethodSecurity // Enables using certain security annotations on service methods
public class ResourceServerSecurityConfig {

    @Autowired
    private JwtAuthenticationProvider jwtAuthProvider;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    AccessDeniedFilter accessDeniedFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        // @formatter:off
        security
            // Allow CORS requests
            .cors(Customizer.withDefaults())
            // CSRF protection not necessary since JWT is used
            .csrf(CsrfConfigurer::disable)
            // Define endpoints to protect/ whitelist
            .authorizeHttpRequests(requests -> {
                requests
                    .requestMatchers(HttpMethod.GET, 
                        EP_HEALTH, EP_RECIPES)
                            .permitAll()
                    .requestMatchers(HttpMethod.GET, 
                        EP_PARSE_HTML, EP_PARSE_URL)
                            .hasAuthority("SCOPE_admin")
                    // .requestMatchers("/api/cart/test").permitAll()
                    .requestMatchers("/error").permitAll()
                    .anyRequest().authenticated();
            })
            // Since client requests are validated via JWT
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })            
            // Navigation redirects/routing is controlled by front end
            .requestCache(cache -> {
                cache.requestCache(new NullRequestCache());
            })
            // Setup this Rest server as OAuth resource server
            .oauth2ResourceServer(oauth2 -> {
                oauth2
                    .jwt(jwt -> {
                        jwt
                            .authenticationManager(new ProviderManager(jwtAuthProvider))
                            .decoder(jwtDecoder);
                    });
            })
            .addFilterAt(accessDeniedFilter, BearerTokenAuthenticationFilter.class)
            ;
            // @formatter: on

        return security.build();
    }
}
