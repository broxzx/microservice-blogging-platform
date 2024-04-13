package com.messageservice.messageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The MessageNotFoundException class is an exception that is thrown when a message is not found.
 * It is a subclass of RuntimeException.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
