package com.marceljsh.binfood.model.repository.spec;

import com.marceljsh.binfood.model.entity.Order;
import com.marceljsh.binfood.model.entity.OrderDetail;
import com.marceljsh.binfood.model.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderDetailRepository {

  boolean save(Order order, Product product, int quantity);

  List<OrderDetail> findAll();

  Optional<OrderDetail> findById(UUID id);

  List<OrderDetail> findByOrderId(UUID orderId);

  boolean existById(UUID id);

  int size();

  void clear();
}
