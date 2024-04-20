package com.marceljsh.binfood.model.repository.spec;

import com.marceljsh.binfood.model.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

  boolean save(Order order);
  Optional<Order> findById(UUID id);
  List<Order> findAll();
  List<Order> findAllCompleted();
  List<Order> findAllUncompleted();
  List<Order> findAllByUserId(UUID userId);
  void deleteById(UUID id);
  boolean existById(UUID id);
  int size();
  void clear();
}
