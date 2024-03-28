package com.example.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenIsAbsent extends RuntimeException {
    public TokenIsAbsent(String message) {
        super(message);
    }
}
