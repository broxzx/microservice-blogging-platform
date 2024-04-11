package com.example.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenIsNotValidException extends RuntimeException{
    public TokenIsNotValidException(String message) {
        super(message);
    }
}
