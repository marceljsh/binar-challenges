package com.marceljsh.binarfud.orderdetail.service;

import com.marceljsh.binarfud.orderdetail.dto.OrderDetailAddRequest;
import com.marceljsh.binarfud.orderdetail.dto.OrderDetailResponse;

import java.util.Set;
import java.util.UUID;

public interface OrderDetailService {

  OrderDetailResponse save(OrderDetailAddRequest request);

  OrderDetailResponse get(UUID id);

  Set<OrderDetailResponse> getByOrderId(UUID orderId);

}
