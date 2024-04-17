package com.example.security.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The JwtTokenProvider class is responsible for generating and validating JWT tokens.
 * It uses a secret key and a duration to generate the tokens.
 */
@Component
@ConditionalOnProperty(prefix = "security", name = "jwt.enabled")
public class JwtTokenProvider {

    @Value("${jwt.token}")
    private String secretKey;

    @Value("${jwt.duration}")
    private Duration duration;

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails The UserDetail object containing user information
     * @return The generated JWT token
     */
    public String getToken(UserDetails userDetails) {
        HashMap<String, List<String>> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", roles);

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + duration.toMillis());

        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(getSecretKey())
                .compact();
    }


    /**
     * Retrieves the username associated with the provided JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     * @throws Exception If an error occurs while processing the token.
     */
    public String getUsernameByToken(String token) throws Exception {
        JwtConsumer consumer = new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build();
        JwtClaims claims = consumer.processToClaims(token);
        return (String) claims.getClaimValue("preferred_username");
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
