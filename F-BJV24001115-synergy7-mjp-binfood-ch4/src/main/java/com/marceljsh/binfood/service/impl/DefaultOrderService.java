package com.marceljsh.binfood.service.impl;

import com.marceljsh.binfood.model.Order;
import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.response.OrderResponse;
import com.marceljsh.binfood.repository.OrderRepo;
import com.marceljsh.binfood.service.spec.OrderService;
import com.marceljsh.binfood.service.spec.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Component
public class DefaultOrderService implements OrderService {

  private final OrderRepo orderRepo;

  private final ValidationService validationService;

  @Autowired
  public DefaultOrderService(OrderRepo orderRepo, ValidationService validationService) {
    this.orderRepo = orderRepo;
    this.validationService = validationService;
  }

  private OrderResponse toOrderResponse(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .userId(order.getUser().getId())
        .createdAt(order.getCreatedAt())
        .updatedAt(order.getUpdatedAt())
        .build();
  }

  @Transactional
  @Override
  public OrderResponse add(User user) {
    Order order = Order.builder()
      .user(user)
      .completed(false)
      .build();

    Order other = orderRepo.save(order);
    if (!order.equals(other)) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to save order");
    }

    return toOrderResponse(order);
  }

  @Transactional(readOnly = true)
  @Override
  public OrderResponse findById(String idString) {
    if (!validationService.isValidUUID(idString)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid id");
    }

    UUID id = UUID.fromString(idString);
    Order order = orderRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));
    return toOrderResponse(order);
  }

  @Transactional
  @Override
  public void remove(String idString) {
    if (!validationService.isValidUUID(idString)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid id");
    }

    UUID id = UUID.fromString(idString);
    orderRepo.deleteById(id);
  }

  @Transactional
  @Override
  public void complete(String idString) {
    if (!validationService.isValidUUID(idString)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid id");
    }

    UUID id = UUID.fromString(idString);
    orderRepo.updateStatus(id, true);
  }
}
