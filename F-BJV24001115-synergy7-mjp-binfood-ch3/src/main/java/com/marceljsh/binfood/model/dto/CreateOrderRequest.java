package com.marceljsh.binfood.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderRequest {

  @NotBlank
  private String destination;

  @NotBlank
  private String customerId;
}
