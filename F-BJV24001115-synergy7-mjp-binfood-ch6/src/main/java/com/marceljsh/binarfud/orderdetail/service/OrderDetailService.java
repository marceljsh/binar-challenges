package com.marceljsh.binarfud.orderdetail.service;

import com.marceljsh.binarfud.orderdetail.dto.OrderDetailAddRequest;
import com.marceljsh.binarfud.orderdetail.dto.OrderDetailResponse;

import java.util.List;
import java.util.UUID;

public interface OrderDetailService {

  OrderDetailResponse save(OrderDetailAddRequest request);

  OrderDetailResponse get(UUID id);

  List<OrderDetailResponse> getByOrderId(UUID orderId);

}
