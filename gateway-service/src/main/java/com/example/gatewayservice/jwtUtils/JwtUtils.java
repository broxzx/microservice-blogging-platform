package com.example.gatewayservice.jwtUtils;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
