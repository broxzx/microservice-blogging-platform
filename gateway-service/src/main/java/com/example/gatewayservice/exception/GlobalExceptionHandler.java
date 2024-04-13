package com.example.gatewayservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The GlobalExceptionHandler class is responsible for handling exceptions globally in the application.
 * It provides exception handling methods for TokenIsNotValidException, TokenIsAbsentException
 * and AccessDeniedException, returning appropriate error responses.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles the TokenIsNotValidException and returns a ResponseEntity with a ProblemDetail object.
     *
     * @param exception The TokenIsNotValidException that was thrown.
     * @return A ResponseEntity containing a ProblemDetail object for the UNAUTHORIZED status with detailed
     *         information indicating that the token is not valid and providing a suggestion to retake the token
     *         by making a request to <a href="http://localhost:8080/security/login">...</a>.
     */
    @ExceptionHandler(TokenIsNotValidException.class)
    public ResponseEntity<ProblemDetail> handleTokenIsNotValidException(TokenIsNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                        "token is not valid. try to retake token by making a request to the http://localhost:8080/security/login");
        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(problemDetail);
    }

    /**
     * Handles the TokenIsAbsentException and returns a ResponseEntity with a ProblemDetail object.
     *
     * @param exception The TokenIsAbsentException that was thrown.
     * @return A ResponseEntity containing a ProblemDetail object for the UNAUTHORIZED status with detailed
     *         information indicating that the jwt token is missing and providing a suggestion to make a request
     *         to <a href="http://localhost:8080/security/login">...</a>.
     */
    @ExceptionHandler(TokenIsAbsentException.class)
    public ResponseEntity<ProblemDetail> handleTokenIsAbsentException(TokenIsAbsentException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                        "jwt token is missing. try to make a request to the http://localhost:8080/security/login");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(problemDetail);
    }

    /**
     * Handles the AccessDeniedException and returns a ResponseEntity containing a ProblemDetail
     *
     * @param accessDenied the AccessDeniedException that occurred
     * @return a ResponseEntity containing a ProblemDetail with the appropriate status and details
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException accessDenied) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.FORBIDDEN, "you don't have an access to this page");

        problemDetail.setProperty("errors", accessDenied.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(problemDetail);
    }
}
