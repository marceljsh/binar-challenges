package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.OrderAddRequest;
import com.marceljsh.binarfud.payload.response.OrderResponse;

import java.util.UUID;

public interface OrderService {

  OrderResponse save(OrderAddRequest request);
  void archive(UUID id);
  void restore(UUID id);
  OrderResponse get(UUID id);
  void remove(UUID id);
  void complete(UUID id);
}
