package com.recipeshopper.jwtauthserver.config.security;

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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import static com.recipeshopper.jwtauthserver.Utils.Urls.*;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthFilterChainConfig {

    @Autowired
    private DaoAuthenticationProvider daoAuthProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthProvider;

    @Autowired
    private JwtDecoder jwtDecoder;

    // @Autowired
    // private LogoutHandler logoutHandler;

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
                        EP_SIGN_IN_DEFAULT,
                        "/auth/key-uri/**"
                            ).permitAll()
                    .requestMatchers(HttpMethod.POST,
                        EP_REGISTER
                            ).permitAll()
                    .requestMatchers("/error").permitAll()
                    // .requestMatchers(HttpMethod.POST, 
                    //     EP_SIGN_IN_BASIC
                    //         ).authenticated()
                    .anyRequest().authenticated(); 
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .formLogin(login -> {
                login
                    .loginPage(EP_SIGN_IN_DEFAULT)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successForwardUrl(EP_AUTHENTICATED);  
            })
            .oauth2ResourceServer(oauth2 -> {
                oauth2
                    .jwt(jwt -> {
                        jwt
                        .authenticationManager(new ProviderManager(jwtAuthProvider))
                        .decoder(jwtDecoder);
                    });  
            })
            .authenticationProvider(daoAuthProvider)
            ;
            // .httpBasic(withDefaults())
        // @formatter:on

        return security.build();
    }
}
