package com.marceljsh.binarfud.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

  private String error;

  public static ErrorResponse from(String error) {
    return ErrorResponse.builder().error(error).build();
  }

}
