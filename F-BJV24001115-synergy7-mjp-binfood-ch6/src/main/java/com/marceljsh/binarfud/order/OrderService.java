package com.marceljsh.binarfud.order;

import java.util.UUID;

public interface OrderService {

  OrderResponse save(OrderAddRequest request);

  OrderResponse get(UUID id);

  void complete(UUID id);

}
