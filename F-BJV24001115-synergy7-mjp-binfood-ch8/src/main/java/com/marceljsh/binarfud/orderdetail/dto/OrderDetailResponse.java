package com.marceljsh.binarfud.orderdetail.dto;

import com.marceljsh.binarfud.orderdetail.model.OrderDetail;
import com.marceljsh.binarfud.product.dto.ProductResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderDetailResponse {

  private UUID id;

  private ProductResponse product;

  private int quantity;

  private BigDecimal totalPrice;

  public static OrderDetailResponse from(OrderDetail orderDetail) {
    return OrderDetailResponse.builder()
        .id(orderDetail.getId())
        .product(ProductResponse.from(orderDetail.getProduct()))
        .quantity(orderDetail.getQuantity())
        .totalPrice(orderDetail.getTotalPrice())
        .build();
  }

}
