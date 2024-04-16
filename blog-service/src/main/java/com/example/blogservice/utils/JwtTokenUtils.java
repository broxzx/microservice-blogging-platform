package com.example.blogservice.utils;

import com.example.blogservice.exception.TokenIsInvalidException;
import org.springframework.stereotype.Component;

/**
 * The JwtTokenUtils class provides utility methods for working with JWT tokens.
 */
@Component
public class JwtTokenUtils {

    /**
     * Verifies the given JWT token.
     *
     * @param token The JWT token to be verified.
     * @return The verified token.
     * @throws TokenIsInvalidException If the token is invalid or absent.
     */
    public String verifyToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            throw new TokenIsInvalidException("token was invalid or absent");
        }

        return token;
    }

}
