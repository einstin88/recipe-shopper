package com.recipeshopper.jwtauthserver.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.recipeshopper.jwtauthserver.model.Token;

@Repository
public class TokenRepo {
    @Autowired
    private RedisTemplate<String, Token> template;

    public Boolean insertToken(String username, Token token) {
        template.opsForValue()
                .set(
                        username,
                        token,
                        Duration.ofMinutes(60));

        if (findToken(username).isPresent())
            return true;

        return false;
    }

    public Optional<Token> findToken(String username) {
        return Optional.ofNullable(
                template.opsForValue()
                        .get(username));
    }

    public void removeToken(String username) {
        template.opsForValue()
                .getAndDelete(username);
    }
}
