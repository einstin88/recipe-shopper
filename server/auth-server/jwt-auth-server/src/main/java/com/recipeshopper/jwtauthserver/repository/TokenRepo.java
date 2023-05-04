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

    public Boolean insertToken(Token token) {
        String tokenVal = token.token().serialize();
        template.opsForValue()
                .set(
                        tokenVal,
                        token,
                        Duration.ofMinutes(10));

        if (findToken(tokenVal).isPresent())
            return true;

        return false;
    }

    public Optional<Token> findToken(String token) {
        return Optional.ofNullable(template.opsForValue()
                .get(token));
    }
}
