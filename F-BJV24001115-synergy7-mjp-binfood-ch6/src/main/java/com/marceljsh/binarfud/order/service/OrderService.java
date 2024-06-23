package com.marceljsh.binarfud.order.service;

import com.marceljsh.binarfud.order.dto.OrderAddRequest;
import com.marceljsh.binarfud.order.dto.OrderResponse;

import java.util.UUID;

public interface OrderService {

  OrderResponse save(OrderAddRequest request);

  OrderResponse get(UUID id);

  void complete(UUID id);

}
