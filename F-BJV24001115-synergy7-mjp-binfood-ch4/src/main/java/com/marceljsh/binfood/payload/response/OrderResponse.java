package com.marceljsh.binfood.payload.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

  private UUID id;

  private UUID userId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
