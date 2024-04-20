package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateOrderRequest;
import com.marceljsh.binfood.model.entity.Order;
import com.marceljsh.binfood.model.entity.User;
import com.marceljsh.binfood.model.repository.impl.InMemoryOrderRepository;
import com.marceljsh.binfood.model.repository.impl.InMemoryUserRepository;
import com.marceljsh.binfood.model.repository.spec.OrderRepository;
import com.marceljsh.binfood.model.repository.spec.UserRepository;
import com.marceljsh.binfood.util.Verifier;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class OrderService {

  private static OrderService instance;

  private final OrderRepository orderRepository;

  private final UserRepository userRepository;

  private final Verifier verifier;

  private OrderService() {
    orderRepository = InMemoryOrderRepository.getInstance();
    userRepository = InMemoryUserRepository.getInstance();
    verifier = Verifier.getInstance();
  }

  public static OrderService getInstance() {
    if (instance == null) {
      instance = new OrderService();
    }
    return instance;
  }

  public boolean save(CreateOrderRequest request) {
    Set<ConstraintViolation<CreateOrderRequest>> violations = verifier.verify(request);
    if (!violations.isEmpty()) {
      return false;
    }

    if (!verifier.isValidUUID(request.getCustomerId())) {
      return false;
    }

    UUID customerId = UUID.fromString(request.getCustomerId());
    User customer = userRepository.findById(customerId).orElse(null);
    if (customer == null) {
      return false;
    }

    return orderRepository.save(new Order(
        null,
        null,
        request.getDestination(),
        customer,
        false));
  }

  public boolean completeOrder(UUID id) {
    Order order = orderRepository.findById(id).orElse(null);
    if (order == null) {
      return false;
    }

    order.setCompleted(true);
    orderRepository.save(order);
    return true;
  }

  public Optional<Order> findById(String id) {
    if (!verifier.isValidUUID(id)) {
      return Optional.empty();
    }

    UUID uuid = UUID.fromString(id);
    return orderRepository.findById(uuid);
  }

  public List<Order> findAll() {
    return orderRepository.findAll();
  }

  public List<Order> findAllCompleted() {
    return orderRepository.findAllCompleted();
  }

  public List<Order> findAllUncompleted() {
    return orderRepository.findAllUncompleted();
  }

  public List<Order> findAllByUserId(String userId) {
    if (!verifier.isValidUUID(userId)) {
      return List.of();
    }

    UUID uuid = UUID.fromString(userId);
    return orderRepository.findAllByUserId(uuid);
  }

  public void deleteById(String id) {
    if (!verifier.isValidUUID(id)) {
      return;
    }

    UUID uuid = UUID.fromString(id);
    orderRepository.deleteById(uuid);
  }

  public boolean existById(String id) {
    if (!verifier.isValidUUID(id)) {
      return false;
    }

    UUID uuid = UUID.fromString(id);
    return orderRepository.existById(uuid);
  }

  public int size() {
    return orderRepository.size();
  }

  public void clear() {
    orderRepository.clear();
  }
}
