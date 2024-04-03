package com.example.gatewayservice.jwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtUtils {

    @Value("${jwt.token}")
    private String secretKey;

    public void validate(String token) {
        Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    public List<String> getJwtRoles(String token) {
        return getAllClaims(token)
                .get("roles", List.class);
    }
}
