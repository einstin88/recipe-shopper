package com.recipeshopper.jwtauthserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.recipeshopper.jwtauthserver.Urls.*;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class AuthSecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Bean
    public SecurityFilterChain authFilterChain(HttpSecurity security) throws Exception {
        // @formatter:off
        security
            .cors(withDefaults())
            .csrf(CsrfConfigurer::disable)
            .authorizeHttpRequests(requests -> {
                requests
                    .requestMatchers(HttpMethod.GET, 
                        EP_HEALTH,
                        "/error"
                            ).permitAll()
                    .requestMatchers(HttpMethod.POST, 
                        EP_SIGN_IN
                            ).authenticated()
                    .anyRequest().authenticated(); 
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationManager(new ProviderManager(
                daoAuthenticationProvider
                ));
        // .httpBasic(withDefaults())
        // @formatter:on

        return security.build();
    }
}
