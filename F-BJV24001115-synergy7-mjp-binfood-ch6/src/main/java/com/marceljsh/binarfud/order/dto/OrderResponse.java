package com.marceljsh.binarfud.order.dto;

import com.marceljsh.binarfud.order.model.Order;
import com.marceljsh.binarfud.user.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

  private UUID id;

  private UserResponse owner;

  private LocalDateTime orderTime;

  private boolean completed;

  public static OrderResponse from(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .owner(UserResponse.from(order.getOwner()))
        .orderTime(order.getOrderTime())
        .completed(order.isCompleted())
        .build();
  }

}
