package com.example.blogservice.utils;

import com.example.blogservice.exception.TokenIsInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {

    @Value("${jwt.token}")
    private String secretKey;

    public Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return getAllClaims(token)
                .getSubject();
    }

    public String checkTokenValidity(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            throw new TokenIsInvalidException("token was invalid or absent");
        }

        return token;
    }
}
