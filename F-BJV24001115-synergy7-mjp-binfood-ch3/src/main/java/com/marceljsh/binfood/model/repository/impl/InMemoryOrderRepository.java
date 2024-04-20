package com.marceljsh.binfood.model.repository.impl;

import com.marceljsh.binfood.model.entity.Order;
import com.marceljsh.binfood.model.repository.spec.OrderRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryOrderRepository implements OrderRepository {

  private static InMemoryOrderRepository instance;

  private final Map<UUID, Order> orders;

  private InMemoryOrderRepository() {
    orders = new HashMap<>();
  }

  public static InMemoryOrderRepository getInstance() {
    if (instance == null) {
      instance = new InMemoryOrderRepository();
    }
    return instance;
  }

  @Override
  public boolean save(Order order) {
    if (order.getId() == null) {
      order.setId(UUID.randomUUID());
      order.setOrderTime(LocalDateTime.now());
    }
    return orders.put(order.getId(), order) == null;
  }

  @Override
  public Optional<Order> findById(UUID id) {
    return Optional.ofNullable(orders.get(id));
  }

  @Override
  public List<Order> findAll() {
    return List.copyOf(orders.values());
  }

  @Override
  public List<Order> findAllCompleted() {
    return orders.values().stream()
      .filter(Order::isCompleted)
      .toList();
  }

  @Override
  public List<Order> findAllUncompleted() {
    return orders.values().stream()
      .filter(order -> !order.isCompleted())
      .toList();
  }

  @Override
  public List<Order> findAllByUserId(UUID userId) {
    return orders.values().stream()
      .filter(order -> order.getCustomer().getId().equals(userId))
      .toList();
  }

  @Override
  public void deleteById(UUID id) {
    orders.remove(id);
  }

  @Override
  public boolean existById(UUID id) {
    return orders.containsKey(id);
  }

  @Override
  public int size() {
    return orders.size();
  }

  @Override
  public void clear() {
    orders.clear();
  }
}
