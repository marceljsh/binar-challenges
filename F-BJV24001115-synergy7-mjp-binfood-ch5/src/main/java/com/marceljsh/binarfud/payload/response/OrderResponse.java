package com.marceljsh.binarfud.payload.response;

import com.marceljsh.binarfud.model.Order;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

  private UUID id;

  private UserResponse owner;

  private LocalDateTime createdAt;

  public static OrderResponse of(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .owner(UserResponse.of(order.getOwner()))
        .createdAt(order.getCreatedAt())
        .build();
  }
}
