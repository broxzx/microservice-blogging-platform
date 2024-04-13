package com.example.subscriptionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler is a class that handles global exceptions by providing a centralized error handling mechanism.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the NotFoundException for subscription resources and returns a ResponseEntity
     * containing a ProblemDetail object with the appropriate status and details.
     *
     * @param exception The NotFoundException instance.
     * @return A ResponseEntity object containing a ProblemDetail object with the appropriate status and details.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> subscriptionNotFoundExceptionHandler(NotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED, "such element wasn't found");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
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
