package com.example.gatewayservice.jwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The JwtUtils class provides methods for validating JSON Web Tokens (JWTs) and extracting information from them.
 */
@Component
public class JwtUtils {

    @Value("${jwt.token}")
    private String secretKey;

    /**
     * Validate the given JSON Web Token (JWT).
     *
     * @param token The JSON Web Token to validate.
     */
    public void validate(String token) {
        Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    /**
     * Retrieves the claims from the provided JSON Web Token (JWT).
     *
     * @param token The JSON Web Token from which to retrieve the claims.
     * @return The claims extracted from the JWT.
     */
    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    /**
     * Retrieves the roles from the provided JSON Web Token (JWT).
     *
     * @param token The JSON Web Token from which to retrieve the roles.
     * @return The roles extracted from the JWT.
     */
    public List<String> getJwtRoles(String token) {
        return getAllClaims(token)
                .get("roles", List.class);
    }
}
