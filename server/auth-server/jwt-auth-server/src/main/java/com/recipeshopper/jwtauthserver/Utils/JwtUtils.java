package com.recipeshopper.jwtauthserver.Utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.recipeshopper.jwtauthserver.exception.TokenTransactionException;
import com.recipeshopper.jwtauthserver.model.SCOPES;
import com.recipeshopper.jwtauthserver.model.Token;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {
    public static Token generateJwt(String username, String email) {
        // Part 1
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);
        log.debug(">>> Token header: {}", header.toString());

        // Part 2
        Instant issueInstant = Instant.now();
        Payload claims = new JWTClaimsSet.Builder()
                .issuer(Consts.JWT_ISSUER)
                .subject(username)
                .issueTime(Date.from(issueInstant))
                .expirationTime(Date.from(issueInstant.plus(Consts.JWT_VALIDITY, ChronoUnit.MINUTES)))
                .claim("scope", SCOPES.READ)
                .claim("email", email)
                .build().toPayload();
        log.debug(">>> Token claims: {}", claims.toString());

        // Part 3
        try {
            RSAKey keyPair = new RSAKeyGenerator(2048).generate();

            JWSSigner signer = new RSASSASigner(keyPair);

            JWSObject token = new JWSObject(
                    header,
                    claims);

            // Sign token with the private key
            token.sign(signer);
            String tokenString = token.serialize();
            log.debug(">>> Token after signing: {}", tokenString);

            return new Token(keyPair.toPublicJWK().toJSONObject(), tokenString);

        } catch (JOSEException e) {
            log.error("--- Token generation error for user: ", username);
            throw new TokenTransactionException(e.getMessage());
        }

    }

}
