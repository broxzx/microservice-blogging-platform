package com.messageservice.messageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class represents an exception that is thrown when a token is invalid.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenIsInvalidException extends RuntimeException {

    public TokenIsInvalidException(String message) {
        super(message);
    }
}
