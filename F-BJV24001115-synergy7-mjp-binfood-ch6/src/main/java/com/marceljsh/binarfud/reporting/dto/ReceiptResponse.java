package com.marceljsh.binarfud.reporting.dto;

import com.marceljsh.binarfud.order.dto.OrderResponse;
import com.marceljsh.binarfud.orderdetail.dto.OrderDetailResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReceiptResponse {

  private OrderResponse order;

  private List<OrderDetailResponse> details;

  public static ReceiptResponse of(OrderResponse order, List<OrderDetailResponse> details) {
    return ReceiptResponse.builder()
        .order(order)
        .details(details)
        .build();
  }

}
