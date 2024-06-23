package com.marceljsh.binarfud.app.exception;

import com.marceljsh.binarfud.common.dto.ErrorResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex) {
    String message = ex.getMessage() != null ? ex.getMessage() : "Unknown error occurred";
    ErrorResponse body = ErrorResponse.from(message);

    return ResponseEntity.internalServerError().body(body);
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<ErrorResponse> handleEntityExists(EntityExistsException ex) {
    ErrorResponse body = ErrorResponse.from("Entity already exists: " + ex.getMessage());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ErrorResponse> handleDatabaseError(DataAccessException ex) {
    ErrorResponse body = ErrorResponse.from("Database error occurred: " + ex.getMessage());

    return ResponseEntity.internalServerError().body(body);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
    ErrorResponse body = ErrorResponse.from("Entity not found: " + ex.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
    ErrorResponse body = ErrorResponse.from("Invalid argument: " + e.getMessage());

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(TransactionSystemException.class)
  public ResponseEntity<ErrorResponse> handleTransactionError(TransactionSystemException ex) {
    ErrorResponse body = ErrorResponse.from("Transaction error occurred: " + ex.getMessage());

    return ResponseEntity.internalServerError().body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult()
        .getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining("; "));

    ErrorResponse body = ErrorResponse.from("Validation error: " + message);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    ErrorResponse body = ErrorResponse.from("Validation error: " + ex.getMessage());

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    ErrorResponse body = ErrorResponse.from("Data integrity violation: " + ex.getMessage());

    return ResponseEntity.badRequest().body(body);
  }

}
