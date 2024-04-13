package com.example.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The GlobalExceptionHandler class is a controller advice class that handles global exception
 * handling for the application. It provides exception handling for UserNotFoundException and
 * returns a ResponseEntity object with the appropriate problem detail.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method is an exception handler for UserNotFoundException. It handles UserNotFoundException
     * and returns a ResponseEntity object with the appropriate problem detail.
     *
     * @param exception The UserNotFoundException that was thrown.
     * @return The ResponseEntity object with the appropriate problem detail.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> userNotFoundHandler(UserNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, "user was not found; make sure your enter correct data");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }
}
