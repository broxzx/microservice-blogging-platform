package com.example.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class TokenIsAbsentException extends RuntimeException {
    public TokenIsAbsentException(String message) {
        super(message);
    }
}
