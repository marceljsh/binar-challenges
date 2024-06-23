package com.marceljsh.binarfud.orderdetail;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderDetailAddRequest {

  @org.hibernate.validator.constraints.UUID(
    message = "Order ID must be a valid UUID"
  )
  private UUID orderId;

  @org.hibernate.validator.constraints.UUID(
    message = "Product ID must be a valid UUID"
  )
  private UUID productId;

  @Positive(message = "Quantity must be a positive number")
  private int quantity;

  @PositiveOrZero(message = "Total price must be a positive number")
  private double totalPrice;

}
