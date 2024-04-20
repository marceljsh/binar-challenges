package com.marceljsh.binfood.model.repository.impl;

import com.marceljsh.binfood.model.entity.Order;
import com.marceljsh.binfood.model.entity.OrderDetail;
import com.marceljsh.binfood.model.entity.Product;
import com.marceljsh.binfood.model.repository.spec.OrderDetailRepository;

import java.util.*;

public class InMemoryOrderDetailRepository implements OrderDetailRepository {

  private static InMemoryOrderDetailRepository instance;

  private final Map<UUID, OrderDetail> orderDetails;

  private InMemoryOrderDetailRepository() {
    orderDetails = new HashMap<>();
  }

  public static InMemoryOrderDetailRepository getInstance() {
    if (instance == null) {
      instance = new InMemoryOrderDetailRepository();
    }
    return instance;
  }

  @Override
  public boolean save(Order order, Product product, int quantity) {
    OrderDetail orderDetail = new OrderDetail(
      UUID.randomUUID(),
      order,
      product,
      quantity,
      product.getPrice() * quantity
    );

    return orderDetails.put(orderDetail.getId(), orderDetail) == null;
  }

  @Override
  public List<OrderDetail> findAll() {
    return List.copyOf(orderDetails.values());
  }

  @Override
  public Optional<OrderDetail> findById(UUID id) {
    return Optional.ofNullable(orderDetails.get(id));
  }

  @Override
  public List<OrderDetail> findByOrderId(UUID orderId) {
  return orderDetails.values().stream()
      .filter(orderDetail -> orderDetail.getOrder().getId().equals(orderId))
      .toList();
  }

  @Override
  public boolean existById(UUID id) {
    return orderDetails.containsKey(id);
  }

  @Override
  public int size() {
    return orderDetails.size();
  }

  @Override
  public void clear() {
    orderDetails.clear();
  }
}
