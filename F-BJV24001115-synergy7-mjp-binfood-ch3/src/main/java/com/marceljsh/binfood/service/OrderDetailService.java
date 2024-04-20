package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.entity.Order;
import com.marceljsh.binfood.model.entity.OrderDetail;
import com.marceljsh.binfood.model.entity.Product;
import com.marceljsh.binfood.model.repository.impl.InMemoryOrderRepository;
import com.marceljsh.binfood.model.repository.impl.InMemoryOrderDetailRepository;
import com.marceljsh.binfood.model.repository.impl.InMemoryProductRepository;
import com.marceljsh.binfood.model.repository.spec.OrderDetailRepository;
import com.marceljsh.binfood.model.repository.spec.OrderRepository;
import com.marceljsh.binfood.model.repository.spec.ProductRepository;
import com.marceljsh.binfood.util.Verifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderDetailService {

  private static OrderDetailService instance;

  private final OrderDetailRepository orderDetailRepository;

  private final OrderRepository orderRepository;

  private final ProductRepository productRepository;

  private final Verifier verifier;

  private OrderDetailService() {
    orderDetailRepository = InMemoryOrderDetailRepository.getInstance();
    orderRepository = InMemoryOrderRepository.getInstance();
    productRepository = InMemoryProductRepository.getInstance();
    verifier = Verifier.getInstance();
  }

  public static OrderDetailService getInstance() {
    if (instance == null) {
      instance = new OrderDetailService();
    }
    return instance;
  }

  public boolean save(String orderIdStr, String productIdStr, int quantity) {
    if (!verifier.isValidUUID(orderIdStr)) {
      return false;
    }

    if (!verifier.isValidUUID(productIdStr)) {
      return false;
    }

    if (quantity <= 0) {
      return false;
    }

    UUID orderId = UUID.fromString(orderIdStr);
    Order order = orderRepository.findById(orderId).orElse(null);
    if (order == null) {
      return false;
    }

    UUID productId = UUID.fromString(productIdStr);
    Product product = productRepository.findById(productId).orElse(null);
    if (product == null) {
      return false;
    }

    return orderDetailRepository.save(order, product, quantity);
  }

  public List<OrderDetail> findAll() {
    return orderDetailRepository.findAll();
  }

  public Optional<OrderDetail> findById(String idStr) {
    if (!verifier.isValidUUID(idStr)) {
      return Optional.empty();
    }

    UUID id = UUID.fromString(idStr);
    return orderDetailRepository.findById(id);
  }

  public List<OrderDetail> findByOrderId(String orderIdStr) {
    if (!verifier.isValidUUID(orderIdStr)) {
      return List.of();
    }

    UUID orderId = UUID.fromString(orderIdStr);
    return orderDetailRepository.findByOrderId(orderId);
  }

  public boolean existById(String idStr) {
    if (!verifier.isValidUUID(idStr)) {
      return false;
    }

    UUID id = UUID.fromString(idStr);
    return orderDetailRepository.existById(id);
  }

  public int size() {
    return orderDetailRepository.size();
  }

  public void clear() {
    orderDetailRepository.clear();
  }
}
