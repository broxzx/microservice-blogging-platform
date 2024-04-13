package com.example.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing an invalid token.
 * This exception is thrown when a token is found to be invalid.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenIsNotValidException extends RuntimeException{
    public TokenIsNotValidException(String message) {
        super(message);
    }
}
