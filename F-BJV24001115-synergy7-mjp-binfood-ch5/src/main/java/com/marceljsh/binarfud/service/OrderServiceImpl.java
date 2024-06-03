package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.model.Order;
import com.marceljsh.binarfud.model.User;
import com.marceljsh.binarfud.payload.request.OrderAddRequest;
import com.marceljsh.binarfud.payload.response.OrderResponse;
import com.marceljsh.binarfud.repository.OrderRepository;
import com.marceljsh.binarfud.repository.UserRepository;
import com.marceljsh.binarfud.service.spec.OrderService;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepo;

  private final UserRepository userRepo;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepo, UserRepository userRepo) {
    this.orderRepo = orderRepo;
    this.userRepo = userRepo;
  }

  @Transactional
  @Override
  public OrderResponse save(OrderAddRequest request) {
    UUID ownerId = UUID.fromString(request.getOwnerId());
    User owner = userRepo.findById(ownerId)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.USER_NOT_FOUND));

    Order order = Order.builder()
      .completed(false)
      .owner(owner)
      .build();

    Order other = orderRepo.save(order);
    if (!order.equals(other)) {
      throw new RuntimeException("failed to create order");
    }

    return OrderResponse.of(order);
  }

  @Transactional
  @Override
  public void archive(UUID id) {
    orderRepo.softDelete(id);
  }

  @Transactional
  @Override
  public void restore(UUID id) {
    orderRepo.restore(id);
  }

  @Transactional
  @Override
  public void remove(UUID id) {
    orderRepo.deleteById(id);
  }

  @Transactional
  @Override
  public OrderResponse get(UUID id) {
    Order order = orderRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.ORDER_NOT_FOUND));
    return OrderResponse.of(order);
  }

  @Transactional
  @Override
  public void complete(UUID id) {
    if (!orderRepo.existsById(id)) {
      throw new EntityNotFoundException(Constants.Msg.ORDER_NOT_FOUND);
    }

    Order order = orderRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.ORDER_NOT_FOUND));

    order.setCompleted(true);
    Order other = orderRepo.save(order);

    if (!order.equals(other) || !other.isCompleted()) {
      throw new TransactionSystemException("failed to complete order");
    }
  }
}
