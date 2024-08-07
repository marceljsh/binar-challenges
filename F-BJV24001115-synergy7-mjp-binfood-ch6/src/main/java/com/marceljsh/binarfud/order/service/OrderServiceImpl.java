package com.marceljsh.binarfud.order.service;

import com.marceljsh.binarfud.app.util.Constants;
import com.marceljsh.binarfud.order.dto.OrderAddRequest;
import com.marceljsh.binarfud.order.dto.OrderResponse;
import com.marceljsh.binarfud.order.model.Order;
import com.marceljsh.binarfud.order.repository.OrderRepository;
import com.marceljsh.binarfud.user.model.User;
import com.marceljsh.binarfud.user.repositoy.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

  private final OrderRepository orderRepo;

  private final UserRepository userRepo;

  @Override
  @Transactional
  public OrderResponse save(OrderAddRequest request) {
    log.trace("Saving order for user ownerId={}", request.getOwnerId());

    User owner = userRepo.findById(request.getOwnerId()).orElse(null);
    if (owner == null) {
      log.error("User not found with id={}", request.getOwnerId());
      throw new EntityNotFoundException(Constants.MSG_USER_NOT_FOUND);
    }

    Order order = Order.builder()
        .orderTime(LocalDateTime.now())
        .destination(request.getDestination())
        .owner(owner)
        .completed(false)
        .build();

    log.info("Saving new order for user @{}", owner.getUsername());

    return OrderResponse.from(orderRepo.save(order));
  }

  @Override
  @Transactional(readOnly = true)
  public OrderResponse get(UUID id) {
    log.trace("Fetching order with id: {}", id);

    Order order = orderRepo.findById(id).orElse(null);
    if (order == null) {
      log.error("Order not found with id: {}", id);
      throw new EntityNotFoundException(Constants.MSG_ORDER_NOT_FOUND);
    }

    log.info("Found order with id: {}", id);

    return OrderResponse.from(order);
  }

  @Override
  @Transactional
  public void complete(UUID id) {
    log.trace("Completing order with id: {}", id);

    if (orderRepo.existsById(id)) {
      log.info("Proceeding to complete order with id: {}", id);
      orderRepo.completeById(id);
    }
  }

}
