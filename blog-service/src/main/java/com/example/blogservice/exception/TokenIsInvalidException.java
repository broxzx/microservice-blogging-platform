package com.example.blogservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenIsInvalidException extends RuntimeException{

    public TokenIsInvalidException(String message) {
        super(message);
    }
}
