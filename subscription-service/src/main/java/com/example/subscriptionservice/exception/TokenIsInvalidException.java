package com.example.subscriptionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class TokenIsInvalidException extends RuntimeException {

    public TokenIsInvalidException(String message) {
        super(message);
    }
}