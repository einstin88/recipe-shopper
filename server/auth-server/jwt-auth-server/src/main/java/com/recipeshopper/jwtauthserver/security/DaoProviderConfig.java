package com.recipeshopper.jwtauthserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.recipeshopper.jwtauthserver.service.AppUserAuthenticationService;

@Configuration
public class DaoProviderConfig {
    @Autowired
    private AppUserAuthenticationService userSvc;

    /**
     * For Validating authentication with username & password
     * 
     * @see <a href=
     *      "https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html">Documentation</a>
     */
    @Bean
    public DaoAuthenticationProvider defaultAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userSvc);
        authProvider.setPasswordEncoder(pwEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder pwEncoder() {
        return new BCryptPasswordEncoder();
    }
}
