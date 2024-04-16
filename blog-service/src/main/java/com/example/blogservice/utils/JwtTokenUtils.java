package com.example.blogservice.utils;

import com.example.blogservice.exception.TokenIsInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The JwtTokenUtils class provides utility methods for working with JWT tokens.
 */
@Component
public class JwtTokenUtils {

    @Value("${jwt.token}")
    private String secretKey;

    /**
     * Retrieves the claims from a JWT token.
     *
     * @param token the JWT token to parse
     * @return the claims extracted from the token
     * @throws TokenIsInvalidException if the token is invalid or absent
     */
    public Claims getAllClaims(String token) {
        System.out.println("here");
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    /**
     * Retrieves the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String getUsername(String token) {
        System.out.println("here");
        return getAllClaims(token)
                .getSubject();
    }

    /**
     * Verifies the given JWT token.
     *
     * @param token The JWT token to be verified.
     * @return The verified token.
     * @throws TokenIsInvalidException If the token is invalid or absent.
     */
    public String verifyToken(String token) {
        System.out.println("here");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            throw new TokenIsInvalidException("token was invalid or absent");
        }

        return token;
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
}
