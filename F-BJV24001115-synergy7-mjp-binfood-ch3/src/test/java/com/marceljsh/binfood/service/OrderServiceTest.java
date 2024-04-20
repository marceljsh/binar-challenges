package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateOrderRequest;
import com.marceljsh.binfood.model.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

  private final OrderService orderService = OrderService.getInstance();

  private final UserService userService = UserService.getInstance();

  UUID seedAndGetIdOfCustomer() {
    CreateUserRequest userRequest = CreateUserRequest.builder()
        .name("Kendrick Lamar")
        .email("kdot@tde.com")
        .password("itsjustbigme")
        .build();
    userService.save(userRequest);

    return userService.findAll().get(0).getId();
  }

  @BeforeEach
  void setUp() {
    orderService.clear();
  }

  @Test
  void saveSuccess() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();

    boolean result = orderService.save(request);
    assertTrue(result);

    UUID orderId = orderService.findAll().get(0).getId();
    assertTrue(orderService.existById(orderId.toString()));
  }

  @Test
  void saveFailedEmptyDestination() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("")
        .customerId(customerId.toString())
        .build();

    boolean result = orderService.save(request);
    assertFalse(result);
  }

  @Test
  void saveFailedInvalidCustomerId() {
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId("invalid-uuid")
        .build();

    boolean result = orderService.save(request);
    assertFalse(result);
  }

  @Test
  void saveFailedNonexistentCustomer() {
    UUID customerId = UUID.randomUUID();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();

    boolean result = orderService.save(request);
    assertFalse(result);
  }

  @Test
  void completeSuccess() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();
    orderService.save(request);

    UUID orderId = orderService.findAll().get(0).getId();
    boolean result = orderService.completeOrder(orderId);
    assertTrue(result);
  }

  @Test
  void completeFailedNonexistentOrder() {
    UUID orderId = UUID.randomUUID();
    boolean result = orderService.completeOrder(orderId);
    assertFalse(result);
  }

  @Test
  void findByIdSuccess() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();
    boolean result = orderService.save(request);

    assertTrue(result);

    UUID orderId = orderService.findAll().get(0).getId();
    assertTrue(orderService.findById(orderId.toString()).isPresent());
  }

  @Test
  void findByIdFailedInvalidId() {
    assertFalse(orderService.findById("invalid-uuid").isPresent());
  }

  @Test
  void findAll() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();
    orderService.save(request);

    assertEquals(1, orderService.findAll().size());
  }

  @Test
  void findAllCompleted() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();
    orderService.save(request);

    UUID orderId = orderService.findAll().get(0).getId();
    orderService.completeOrder(orderId);

    assertEquals(1, orderService.findAllCompleted().size());
  }

  @Test
  void findAllUncompleted() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();
    orderService.save(request);

    assertEquals(1, orderService.findAllUncompleted().size());
  }

  @Test
  void findAllByUserIdSuccess() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();
    orderService.save(request);

    assertEquals(1, orderService.findAllByUserId(customerId.toString()).size());
  }

  @Test
  void findAllByUserIdFailedInvalidId() {
    assertEquals(0, orderService.findAllByUserId("invalid-uuid").size());
  }

  @Test
  void deleteByIdSuccess() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();
    orderService.save(request);

    UUID orderId = orderService.findAll().get(0).getId();
    orderService.deleteById(orderId.toString());

    assertEquals(0, orderService.findAll().size());
  }

  @Test
  void deleteByIdFailedInvalidId() {
    orderService.deleteById("invalid-uuid");
    assertEquals(0, orderService.size());
  }

  @Test
  void deleteByIdFailedNonexistentOrder() {
    UUID orderId = UUID.randomUUID();
    orderService.deleteById(orderId.toString());
    assertEquals(0, orderService.size());
  }

  @Test
  void existByIdSuccess() {
    UUID customerId = seedAndGetIdOfCustomer();
    CreateOrderRequest request = CreateOrderRequest.builder()
        .destination("Egghead")
        .customerId(customerId.toString())
        .build();
    orderService.save(request);

    UUID orderId = orderService.findAll().get(0).getId();
    assertTrue(orderService.existById(orderId.toString()));
  }

  @Test
  void existByIdFailedInvalidId() {
    assertFalse(orderService.existById("invalid-uuid"));
  }

  @Test
  void existByIdFailedNonexistentOrder() {
    UUID orderId = UUID.randomUUID();
    assertFalse(orderService.existById(orderId.toString()));
  }
}