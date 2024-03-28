package com.example.gatewayservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenIsNotValid extends RuntimeException{
    public TokenIsNotValid(String message) {
        super(message);
    }
}
