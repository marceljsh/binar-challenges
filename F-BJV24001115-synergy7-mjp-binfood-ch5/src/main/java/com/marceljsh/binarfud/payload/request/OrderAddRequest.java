package com.marceljsh.binarfud.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
public class OrderAddRequest {

  @NotBlank(message = "owner_id cannot be empty")
  @UUID(message = "owner_id must be a valid UUID")
  private String ownerId;
}
