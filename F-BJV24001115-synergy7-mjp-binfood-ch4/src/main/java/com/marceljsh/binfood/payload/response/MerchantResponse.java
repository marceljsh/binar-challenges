package com.marceljsh.binfood.payload.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MerchantResponse {

  private UUID id;

  private String name;

  private String location;

  private boolean open;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
