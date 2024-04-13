package com.example.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing the absence of a token.
 * This exception is thrown when a token is expected but missing.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenIsAbsentException extends RuntimeException {
    public TokenIsAbsentException(String message) {
        super(message);
    }
}
