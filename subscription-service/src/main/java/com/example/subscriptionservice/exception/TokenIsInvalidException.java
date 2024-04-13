package com.example.subscriptionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception representing an invalid token.
 * This exception is thrown when a token is either invalid or absent.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenIsInvalidException extends RuntimeException {

    public TokenIsInvalidException(String message) {
        super(message);
    }
}
