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
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ResourceServerSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .cors(Customizer.withDefaults())
                .csrf(csrf -> {
                    csrf.disable();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(HttpMethod.GET, "/api/health", "/api/recipes").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/parse-url","/api/parse-html").hasAuthority("SCOPE_admin")
                            .anyRequest().authenticated();
                })
                .requestCache(cache -> {
                    cache.requestCache(new NullRequestCache());
                })
                .oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(Customizer.withDefaults());
                });

        return security.build();
    }
}
