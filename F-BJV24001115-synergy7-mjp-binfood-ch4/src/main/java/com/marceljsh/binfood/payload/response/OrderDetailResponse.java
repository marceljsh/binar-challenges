package com.marceljsh.binfood.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderDetailResponse {

  private UUID id;

  private OrderResponse order;

  private ProductResponse product;

  private int quantity;

  private long totalPrice;
}
