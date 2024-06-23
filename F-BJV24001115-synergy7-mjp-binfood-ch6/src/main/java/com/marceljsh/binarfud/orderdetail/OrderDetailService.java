package com.marceljsh.binarfud.orderdetail;

import java.util.Set;
import java.util.UUID;

public interface OrderDetailService {

  OrderDetailResponse save(OrderDetailAddRequest request);

  OrderDetailResponse get(UUID id);

  Set<OrderDetailResponse> getByOrderId(UUID orderId);

}
