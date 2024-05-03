package com.marceljsh.binfood.service.spec;

import com.marceljsh.binfood.payload.request.OrderDetailAddRequest;
import com.marceljsh.binfood.payload.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {

  OrderDetailResponse add(OrderDetailAddRequest request);
  List<OrderDetailResponse> findByOrderId(String orderIdString);
}
