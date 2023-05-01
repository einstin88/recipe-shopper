package com.recipeshopper.authserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

public class OAuthClientRepository implements RegisteredClientRepository {

    @Autowired
    private RedisTemplate<String, Object> template;

    @Override
    @Nullable
    public RegisteredClient findByClientId(String arg0) {
        throw new UnsupportedOperationException("Unimplemented method 'findByClientId'");
    }

    @Override
    @Nullable
    public RegisteredClient findById(String arg0) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient must not be null");
    }
    
}
