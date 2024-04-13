package com.messageservice.messageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * GlobalExceptionHandler is a class that handles global exceptions in the application.
 * It provides exception handling methods for specific exceptions and returns appropriate error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method handles the AccessDeniedException and returns an appropriate error response.
     *
     * @param accessDeniedException The AccessDeniedException that was thrown.
     * @return ResponseEntity<ProblemDetail> The ResponseEntity containing the error response.
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

    /**
     * This method is an exception handler for handling the MessageNotFoundException.
     * It generates a ProblemDetail object with the appropriate status and detail, and sets the "errors" property to the exception message.
     * It returns a ResponseEntity containing the ProblemDetail object and the HttpStatus.NOT_FOUND status code.
     *
     * @param exception The MessageNotFoundException that was thrown.
     * @return ResponseEntity<ProblemDetail> The ResponseEntity containing the error response.
     */
    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ProblemDetail> messageNotFoundExceptionHandler(MessageNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, "message was not found");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    /**
     * This method is an exception handler for handling the TokenIsInvalidException.
     * It generates a ProblemDetail object with the appropriate status and detail, and sets the "errors" property to the exception message.
     * It returns a ResponseEntity containing the ProblemDetail object and the HttpStatus.UNAUTHORIZED status code.
     *
     * @param exception The TokenIsInvalidException that was thrown.
     * @return ResponseEntity<ProblemDetail> The ResponseEntity containing the error response.
     */
    @ExceptionHandler(TokenIsInvalidException.class)
    public ResponseEntity<ProblemDetail> tokenIsInvalidExceptionHandler(TokenIsInvalidException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED, "your token is invalid");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(problemDetail);
    }
}
