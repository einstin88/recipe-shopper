package com.recipeshopper.jwtauthserver.Utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
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
import com.nimbusds.jwt.JWTClaimsSet;
import com.recipeshopper.jwtauthserver.model.AppUser;
import com.recipeshopper.jwtauthserver.model.SCOPES;
import com.recipeshopper.jwtauthserver.model.Token;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {
    public static Token generateJwt(AppUser user) {
        // Part 1
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);

        // Part 2
        Instant issueInstant = Instant.now();
        Payload claims = new JWTClaimsSet.Builder()
                .issuer(Consts.JWT_ISSUER)
                .subject(user.getUsername())
                .issueTime(Date.from(issueInstant))
                .expirationTime(Date.from(issueInstant.plus(10, ChronoUnit.MINUTES)))
                .claim("scope", SCOPES.READ)
                .build().toPayload();

        // Part 3
        KeyPair keyPair = generateKeyPair();
        PrivateKey privKey = keyPair.getPrivate();
        JWSSigner signer = new RSASSASigner(privKey);

        JWSObject token = new JWSObject(
                header,
                claims);

        try {
            token.sign(signer);
        } catch (JOSEException e) {
            log.error("--- Token could not be signed for user: ", user.getUsername());
        }

        return new Token(keyPair, token);
    }

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keysGen = KeyPairGenerator.getInstance("RSA");
            keysGen.initialize(2048);
            return keysGen.genKeyPair();

        } catch (NoSuchAlgorithmException e) {
            log.error("--- Could not find key-pair generator...");
            throw new IllegalStateException(e);
        }
    }
}
