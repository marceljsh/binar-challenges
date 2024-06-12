package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.OrderDetailAddRequest;
import com.marceljsh.binarfud.payload.response.OrderDetailResponse;

import java.util.List;
import java.util.UUID;

public interface OrderDetailService {

  OrderDetailResponse save(OrderDetailAddRequest request);

  void archive(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  OrderDetailResponse get(UUID id);

  List<OrderDetailResponse> getByOrderId(UUID orderId);
}
