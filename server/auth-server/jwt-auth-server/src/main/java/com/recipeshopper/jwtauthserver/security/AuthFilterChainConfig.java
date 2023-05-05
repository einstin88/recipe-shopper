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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.recipeshopper.jwtauthserver.Utils.Urls.*;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class AuthFilterChainConfig {

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    // @Autowired
    // private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private LogoutHandler logoutHandler;

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
                        EP_SIGN_IN_DEFAULT
                        ).permitAll()
                    .requestMatchers(HttpMethod.POST, 
                        EP_REGISTER
                        ).permitAll()
                    .requestMatchers(HttpMethod.POST, 
                        EP_SIGN_IN_BASIC
                            ).authenticated()
                    .requestMatchers("/error").permitAll()
                    .anyRequest().authenticated(); 
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(login -> {
                login
                    .loginPage(EP_SIGN_IN_DEFAULT)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successForwardUrl(EP_AUTHENTICATED);  
            })
            .authenticationManager(new ProviderManager(
                daoAuthenticationProvider
                ))
            .logout(logoutRequest -> {
                logoutRequest
                    .logoutUrl(EP_LOG_OUT)
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler((request, response, auth) -> {
                        SecurityContextHolder.clearContext();
                    });
            });
            // .httpBasic(withDefaults())
        // @formatter:on

        return security.build();
    }
}
