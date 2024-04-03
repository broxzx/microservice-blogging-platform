package com.example.gatewayservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenIsNotValidException.class)
    public ResponseEntity<ProblemDetail> handleTokenIsNotValidException(TokenIsNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        "token is not valid. try to retake token by making a request to the http://localhost:8080/security/login");
        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }

    @ExceptionHandler(TokenIsAbsentException.class)
    public ResponseEntity<ProblemDetail> handleTokenIsAbsentException(TokenIsAbsentException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        "jwt token is missing. try to make a request to the http://localhost:8080/security/login");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }

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
