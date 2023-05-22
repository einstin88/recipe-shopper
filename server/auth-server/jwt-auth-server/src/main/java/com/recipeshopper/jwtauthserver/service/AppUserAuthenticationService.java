package com.recipeshopper.jwtauthserver.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipeshopper.jwtauthserver.Utils.JwtUtils;
import com.recipeshopper.jwtauthserver.exception.AppUserCreationException;
import com.recipeshopper.jwtauthserver.exception.TokenTransactionException;
import com.recipeshopper.jwtauthserver.model.AppUser;
import com.recipeshopper.jwtauthserver.model.Token;
import com.recipeshopper.jwtauthserver.repository.AppUserRepo;
import com.recipeshopper.jwtauthserver.repository.TokenRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * Handle queries to {@link AppUserRepo} as well as being used by
 * {@link DaoAuthenticationProvider} for user authentication in
 * {@link DaoProviderConfig}
 */
@Service
@Slf4j
public class AppUserAuthenticationService implements UserDetailsService {
    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private AppUserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug(">>> Looking for user: {}", username);
        return userRepo.findUserByUsername(username)
                .orElseThrow(() -> {
                    log.debug(">>> {} is not valid", username);
                    throw new UsernameNotFoundException(username);
                });
    }

    @Transactional
    public String registerNewUser(AppUser newUser) {
        String username = newUser.getUsername();
        // Validation 1: username is available
        if (userRepo.findUserByUsername(username).isPresent()) {
            generateServerError(
                    "User: %s already exists!",
                    AppUserCreationException.class, username);
        }

        // Add user to DB
        if (!userRepo.insertUser(newUser)) {
            generateServerError(
                    "Failed to create user %s",
                    AppUserCreationException.class, username);
        }

        // Get token and save it to Redis
        return generateAndSaveToken(username, newUser.getEmail());
    }

    public String processAuthenticatedUser(String username, String email) {
        Optional<Token> exisitingToken = tokenRepo.findToken(username);
        if (exisitingToken.isPresent())
            return exisitingToken.get().token();

        return generateAndSaveToken(username, email);
    }

    public void removeToken(String username) {
        tokenRepo.removeToken(username);
    }

    private String generateAndSaveToken(String username, String email) {
        Token token = JwtUtils.generateJwt(username, email);
        log.debug(">>> Token generated...");
        if (!tokenRepo.insertToken(username, token))
            generateServerError(
                    "Token not saved for user: %s",
                    TokenTransactionException.class, username);

        return token.token();
    }

    private static <T extends RuntimeException> void generateServerError(
            String errMsg, Class<T> exceptionClass, Object... args) {

        errMsg = errMsg.formatted(args);
        log.debug("--- {}", errMsg);

        try {
            throw exceptionClass
                    .getConstructor(String.class)
                    .newInstance(errMsg);
        } catch (InstantiationException e) {
            log.error("Internal Error! {}", e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("Internal Error! {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Internal Error! {}", e.getMessage());
        } catch (InvocationTargetException e) {
            log.error("Internal Error! {}", e.getMessage());
        } catch (NoSuchMethodException e) {
            log.error("Internal Error! {}", e.getMessage());
        } catch (SecurityException e) {
            log.error("Internal Error! {}", e.getMessage());
        }
    }
}
