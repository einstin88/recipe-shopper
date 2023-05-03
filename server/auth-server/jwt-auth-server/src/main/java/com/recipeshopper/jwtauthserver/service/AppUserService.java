package com.recipeshopper.jwtauthserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.recipeshopper.jwtauthserver.repository.AppUserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppUserService implements UserDetailsService {
    @Autowired
    private AppUserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(">>> Looking for user: {}", username);
        try {
            return repo.findUserByUsername(username);

        } catch (DataAccessException e) {
            log.info(">>> {} is not valid", username);
            throw new UsernameNotFoundException(username);
        }
    }
}
