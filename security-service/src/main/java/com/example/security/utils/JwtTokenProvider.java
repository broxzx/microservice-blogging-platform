package com.example.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The JwtTokenProvider class is responsible for generating and validating JWT tokens.
 * It uses a secret key and a duration to generate the tokens.
 */
@Component
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
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Retrieves the claims from the JWT token.
     *
     * @param token The JWT token
     * @return The claims from the JWT token
     */
    public Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Retrieves the username from the JWT token.
     *
     * @param token The JWT token
     * @return The username retrieved from the JWT token
     */
    public String getUsername(String token) {
        return getAllClaims(token)
                .getSubject();
    }

    /**
     * Retrieves the roles from the JWT token.
     *
     * @param token The JWT token
     * @return A list of roles retrieved from the JWT token
     */
    public List<String> getRoles(String token) {
        return getAllClaims(token)
                .get("roles", List.class);
    }

    /**
     * Validates a JWT token.
     *
     * @param token The JWT token to be validated
     */
    public void validate(String token) {
        Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}
