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

    @ExceptionHandler(TokenIsNotValid.class)
    public ResponseEntity<ProblemDetail> handleTokenIsNotValidException(TokenIsNotValid exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        "token is not valid. try to retake token by making a request to the http://localhost:8080/security/login");
        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }

    @ExceptionHandler(TokenIsAbsent.class)
    public ResponseEntity<ProblemDetail> handleTokenIsAbsentException(TokenIsAbsent exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        "jwt token is missing. try to make a request to the http://localhost:8080/security/login");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }
}
