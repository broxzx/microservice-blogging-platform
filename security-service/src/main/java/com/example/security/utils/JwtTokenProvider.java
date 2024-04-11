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

@Component
public class JwtTokenProvider {

    @Value("${jwt.token}")
    private String secretKey;

    @Value("${jwt.duration}")
    private Duration duration;

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

    public Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return getAllClaims(token)
                .getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaims(token)
                .get("roles", List.class);
    }

    public void validate(String token) {
        Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}
