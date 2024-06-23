package com.marceljsh.binarfud.order;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderAddRequest {

  @org.hibernate.validator.constraints.UUID(
    message = "Owner ID must be a valid UUID"
  )
  private UUID ownerId;

  @Size(
    min = 3,
    max = 255,
    message = "Order destination must be {min}-{max} characters long"
  )
  private String destination;

}
