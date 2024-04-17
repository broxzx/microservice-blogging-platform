package com.example.gatewayservice.jwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The JwtUtils class provides methods for validating JSON Web Tokens (JWTs) and extracting information from them.
 */
@Component
@ConditionalOnProperty(prefix = "jwt", name = "enabled")
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
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
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
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Retrieves the roles from the provided JSON Web Token (JWT).
     *
     * @param token The JSON Web Token from which to retrieve the roles.
     * @return The roles extracted from the JWT.
     */
    public List<String> getJwtRoles(String token) {
        List<?> list = getAllClaims(token).get("roles", List.class);

        if (list != null) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
