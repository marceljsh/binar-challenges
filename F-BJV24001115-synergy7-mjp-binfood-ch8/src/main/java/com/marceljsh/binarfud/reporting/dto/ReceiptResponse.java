package com.marceljsh.binarfud.reporting.dto;

import com.marceljsh.binarfud.order.dto.OrderResponse;
import com.marceljsh.binarfud.orderdetail.dto.OrderDetailResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReceiptResponse {

  private OrderResponse order;

  private BigDecimal grandTotal;

  private List<OrderDetailResponse> details;

  public static ReceiptResponse of(OrderResponse order, List<OrderDetailResponse> details) {
    BigDecimal grandTotal = details.stream()
        .map(OrderDetailResponse::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return ReceiptResponse.builder()
        .order(order)
        .grandTotal(grandTotal)
        .details(details)
        .build();
  }

}
