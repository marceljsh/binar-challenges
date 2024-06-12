package com.marceljsh.binarfud.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
public class OrderDetailAddRequest {

  @NotBlank(message = "order_id cannot be empty")
  @UUID(message = "order_id must be a valid UUID")
  private String orderId;

  @NotBlank(message = "product_id cannot be empty")
  @UUID(message = "product_id must be a valid UUID")
  private String productId;

  @Positive(message = "quantity must be greater than zero")
  private int quantity;
}
