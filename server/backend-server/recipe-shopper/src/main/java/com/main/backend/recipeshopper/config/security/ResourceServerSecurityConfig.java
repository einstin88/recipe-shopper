package com.main.backend.recipeshopper.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.NullRequestCache;

/**
 * Spring Security Configurations - mainly to
 * (1.) define which endpoints are protected/ publicly accessible,
 * (2.) protect endpoints using oath2 (as resource server) protocol,
 * (3.) use jwt for authorization to access protected resources
 * 
 * Authentication is handled by a separate Auth-Server and is the source of
 * truth used by the OAuth resource server to validate JWT claims
 */
@Configuration
@EnableWebSecurity // Denotes class for Spring to import and look for security settings
@EnableMethodSecurity(prePostEnabled = true) // Enables using certain security annotations on service methods
public class ResourceServerSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        // @formatter:off
        security
            // Allow CORS requests
            .cors(Customizer.withDefaults())
            // CSRF protection not necessary since JWT will be used
            .csrf(csrf -> {
                csrf.disable();
            })
            // Since client requests are validated via JWT
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            // Define endpoints to protect
            .authorizeHttpRequests(requests -> {
                requests
                        .requestMatchers(HttpMethod.GET, "/api/health", "/api/recipes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/parse-url", "/api/parse-html")
                        .hasAuthority("SCOPE_admin")
                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                        .anyRequest().authenticated();
            })
            // Navigation redirects/routing is controlled by front end
            .requestCache(cache -> {
                cache.requestCache(new NullRequestCache());
            })
            // Setup this Rest server as OAuth resource server
            .oauth2ResourceServer(oauth2 -> {
                oauth2.jwt(Customizer.withDefaults());
            })
            .exceptionHandling(exception -> {
                exception.authenticationEntryPoint(
                    new LoginUrlAuthenticationEntryPoint("/login")
                );
            });
        // @formatter: on

        return security.build();
    }
}
