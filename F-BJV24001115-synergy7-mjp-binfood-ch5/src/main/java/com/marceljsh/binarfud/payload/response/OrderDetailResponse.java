package com.marceljsh.binarfud.payload.response;

import com.marceljsh.binarfud.model.OrderDetail;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderDetailResponse {

  private UUID id;

  private OrderResponse order;

  private ProductResponse product;

  private int quantity;

  private BigDecimal totalPrice;

  public static OrderDetailResponse of(OrderDetail orderDetail) {
    return OrderDetailResponse.builder()
        .id(orderDetail.getId())
        .order(OrderResponse.of(orderDetail.getOrder()))
        .product(ProductResponse.of(orderDetail.getProduct()))
        .quantity(orderDetail.getQuantity())
        .totalPrice(orderDetail.getTotalPrice())
        .build();
  }
}
