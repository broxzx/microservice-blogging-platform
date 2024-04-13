package com.example.blogservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing an invalid token error.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenIsInvalidException extends RuntimeException{

    public TokenIsInvalidException(String message) {
        super(message);
    }
}
