package com.marceljsh.binfood.controller;

import com.marceljsh.binfood.payload.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<String>> constraintViolationException(ConstraintViolationException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.<String>builder()
            .error(e.getMessage())
            .build());
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<String>> apiException(ResponseStatusException e) {
    return ResponseEntity.status(e.getStatusCode())
        .body(ApiResponse.<String>builder()
            .error(e.getReason())
            .build());
  }
}
