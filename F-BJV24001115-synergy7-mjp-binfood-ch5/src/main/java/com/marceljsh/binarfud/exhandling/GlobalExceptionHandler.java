package com.marceljsh.binarfud.exhandling;

import com.marceljsh.binarfud.payload.response.ErrorResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
    log.error("Resource not found: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    log.error("Illegal argument: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException e) {
    log.error("Entity exists: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(HttpStatus.CONFLICT, e.getMessage());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(TransactionSystemException.class)
  public ResponseEntity<ErrorResponse> handleTransactionSystemException(TransactionSystemException e) {
    log.error("Transaction canceled: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, "invalid request body");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
    log.error("Invalid payload: {}", e.getMessage());

    String errorMessage = e.getBindingResult().getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
    ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, errorMessage);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
    log.error("Constraint violation: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, "invalid request body");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(InternalError.class)
  public ResponseEntity<ErrorResponse> handleInternalError(InternalError e) {
    log.error("Internal error: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<ErrorResponse> handleUnsupportedOperationException(UnsupportedOperationException e) {
    log.error("Unsupported operation: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(PageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlePageNotFoundException(PageNotFoundException e) {
    log.error("Page not found: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }
}
