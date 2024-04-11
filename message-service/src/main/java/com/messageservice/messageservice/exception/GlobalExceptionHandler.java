package com.messageservice.messageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.FORBIDDEN, "you don't have an access to this method");

        problemDetail.setProperty("errors", accessDeniedException.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(problemDetail);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ProblemDetail> messageNotFoundExceptionHandler(MessageNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, "message was not found");

        problemDetail.setProperty("errors", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

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
