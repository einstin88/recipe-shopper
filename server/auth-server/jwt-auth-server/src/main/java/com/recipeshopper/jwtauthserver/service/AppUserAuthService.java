package com.recipeshopper.jwtauthserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JWSObject;
import com.recipeshopper.jwtauthserver.Utils.JwtUtils;
import com.recipeshopper.jwtauthserver.exception.AppUserCreationException;
import com.recipeshopper.jwtauthserver.exception.TokenTransactionException;
import com.recipeshopper.jwtauthserver.model.AppUser;
import com.recipeshopper.jwtauthserver.model.Token;
import com.recipeshopper.jwtauthserver.repository.AppUserRepo;
import com.recipeshopper.jwtauthserver.repository.TokenRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * public DaoAuthenticationProvider defaultAuthProvider() {
 * Handle queries to {@link AppUserRepo} as well as being used by
 * {@link DaoAuthenticationProvider} for user authentication in
 * {@link AuthSupportConfig}
 */
@Service
@Slf4j
public class AppUserAuthService implements UserDetailsService {
    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private AppUserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(">>> Looking for user: {}", username);
        try {
            return userRepo.findUserByUsername(username);

        } catch (DataAccessException e) {
            log.info(">>> {} is not valid", username);
            throw new UsernameNotFoundException(username);
        }
    }

    public JWSObject registerNewUser(AppUser newUser) {
        String username = newUser.getUsername();
        // Validation 1: username is available
        if (loadUserByUsername(username) != null) {
            generateServerError(
                    "User: %s already exists!", AppUserCreationException.class, username);
        }

        // Add user to DB
        if (!userRepo.insertUser(newUser)) {
            generateServerError(
                    "Failed to create user %s", AppUserCreationException.class, username);
        }

        Token token = JwtUtils.generateJwt(newUser);
        if (!tokenRepo.insertToken(token))
            generateServerError(
                    "Token not saved for user: %s", TokenTransactionException.class, username);

        return token.token();
    }

    public void authenticatUser() {

    }

    private <T extends RuntimeException> void generateServerError(
            String errMsg, Class<T> exceptionClass, Object... args) {

        errMsg = errMsg.formatted(args);
        log.info("--- {}", errMsg);
        try {
            throw exceptionClass
                    .getConstructor(String.class)
                    .newInstance(errMsg);
        } catch (Exception e) {
            log.error("Internal Error! {}", e.getMessage());
        }
    }
}
