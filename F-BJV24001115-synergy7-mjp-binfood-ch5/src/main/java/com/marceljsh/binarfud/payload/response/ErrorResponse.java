package com.marceljsh.binarfud.payload.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

  private HttpStatus status;

  private LocalDateTime timestamp;

  private String message;

  public static ErrorResponse of(HttpStatus status, String message) {
    return ErrorResponse.builder()
        .status(status)
        .timestamp(LocalDateTime.now())
        .message(message)
        .build();
  }
}
