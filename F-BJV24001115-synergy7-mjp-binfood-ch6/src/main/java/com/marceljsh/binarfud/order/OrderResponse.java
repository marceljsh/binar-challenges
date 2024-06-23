package com.marceljsh.binarfud.order;

import com.marceljsh.binarfud.user.UserResponse;
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

  public static OrderResponse from(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .owner(UserResponse.from(order.getOwner()))
        .orderTime(order.getOrderTime())
        .build();
  }

}
