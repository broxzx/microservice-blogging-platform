package com.example.blogservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler class to handle various exceptions that may occur in the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler method that handles BlogNotFoundException.
     *
     * @param blogNotFoundException the exception to handle
     * @return a ResponseEntity containing the detailed problem for the exception
     */
    @ExceptionHandler(BlogNotFoundException.class)
    public ResponseEntity<ProblemDetail> blogNotFoundExceptionHandler(BlogNotFoundException blogNotFoundException) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, "blog with such id was not found; make sure you enter correct id");

        problemDetail.setProperty("errors: ", blogNotFoundException.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    /**
     * Exception handler method that handles UserNotFoundException.
     *
     * @param exception the exception to handle
     * @return a ResponseEntity containing the detailed problem for the exception
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> userNotFoundExceptionHandler(UserNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, "user with such data was not found; make sure you enter correct data");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    /**
     * Exception handler method that handles TokenIsInvalidException.
     *
     * @param exception the exception to handle
     * @return a ResponseEntity containing the detailed problem for the exception
     */
    @ExceptionHandler(TokenIsInvalidException.class)
    public ResponseEntity<ProblemDetail> tokenIsInvalidExceptionHandler(TokenIsInvalidException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED, "token was whether invalid or absent");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(problemDetail);
    }

    /**
     * Handles the AccessDeniedException and returns a ResponseEntity with the appropriate problem detail.
     *
     * @param accessDeniedException the AccessDeniedException to handle
     * @return a ResponseEntity containing the detailed problem for the exception
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.FORBIDDEN, "you don't have an access to this method");

        problemDetail.setProperty("errors", accessDeniedException.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(problemDetail);
    }
}
