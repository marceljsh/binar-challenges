package com.marceljsh.binarfud.reporting.service;

import com.marceljsh.binarfud.order.dto.OrderResponse;
import com.marceljsh.binarfud.order.service.OrderService;
import com.marceljsh.binarfud.orderdetail.dto.OrderDetailResponse;
import com.marceljsh.binarfud.orderdetail.service.OrderDetailService;
import com.marceljsh.binarfud.reporting.dto.ReceiptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

  private final OrderService orderService;

  private final OrderDetailService odService;

  @Override
  @Transactional(readOnly = true)
  public ReceiptResponse generateReceipt(UUID orderId) {
    OrderResponse order = orderService.get(orderId);

    List<OrderDetailResponse> details = odService.getByOrderId(orderId);

    return ReceiptResponse.of(order, details);
  }

}
