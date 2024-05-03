package com.marceljsh.binfood.service.spec;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.response.OrderResponse;

public interface OrderService {

  OrderResponse add(User user);
  OrderResponse findById(String idString);
  void remove(String idString);
  void complete(String idString);
}
