package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateMerchantRequest;
import com.marceljsh.binfood.model.dto.CreateOrderRequest;
import com.marceljsh.binfood.model.dto.CreateProductRequest;
import com.marceljsh.binfood.model.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderDetailServiceTest {

  private final OrderDetailService orderDetailService = OrderDetailService.getInstance();

  private final OrderService orderService = OrderService.getInstance();

  private final ProductService productService = ProductService.getInstance();

  private final UserService userService = UserService.getInstance();

  private final MerchantService merchantService = MerchantService.getInstance();

  @BeforeEach
  void setUp() {
    orderDetailService.clear();
    orderService.clear();
    productService.clear();
    userService.clear();
    merchantService.clear();
  }

  UUID seedAndGetIdOfCustomer() {
    CreateUserRequest userRequest = CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdot@tde.com")
      .password("itsjustbigme")
      .build();
    userService.save(userRequest);

    return userService.findAll().get(0).getId();
  }

  UUID seedAndGetIdOfMerchant() {
    CreateMerchantRequest userRequest = CreateMerchantRequest.builder()
      .name("B-Dup da KrackDealah")
      .location("Idlewood")
      .build();
    merchantService.save(userRequest);

    return merchantService.findAll().get(0).getId();
  }

  UUID seedAndGetIdOfOrder() {
    UUID customerId = seedAndGetIdOfCustomer();
    orderService.save(CreateOrderRequest.builder()
        .customerId(customerId.toString())
        .destination("Egghead")
        .build());

    return orderService.findAll().get(0).getId();
  }

  UUID seedAndGetIdOfProduct() {
    UUID merchantId = seedAndGetIdOfMerchant();
    productService.save(CreateProductRequest.builder()
        .name("Fent")
        .price(500L)
        .merchantId(merchantId.toString())
        .build());
    return productService.findAll().get(0).getId();
  }

  @Test
  void saveSuccess() {
    UUID orderId = seedAndGetIdOfOrder();
    UUID productId = seedAndGetIdOfProduct();
    boolean result = orderDetailService.save(orderId.toString(), productId.toString(), 1);
    assertTrue(result);
  }

  @Test
  void saveFailedInvalidOrderId() {
    UUID productId = seedAndGetIdOfProduct();
    boolean result = orderDetailService.save("invalid-uuid", productId.toString(), 1);
    assertFalse(result);
  }

  @Test
  void saveFailedInvalidProductId() {
    UUID orderId = seedAndGetIdOfOrder();
    boolean result = orderDetailService.save(orderId.toString(), "invalid-uuid", 1);
    assertFalse(result);
  }

  @Test
  void saveFailedNonexistentOrder() {
    UUID productId = seedAndGetIdOfProduct();
    boolean result = orderDetailService.save(UUID.randomUUID().toString(), productId.toString(), 1);
    assertFalse(result);
  }

  @Test
  void saveFailedNonexistentProduct() {
    UUID orderId = seedAndGetIdOfOrder();
    boolean result = orderDetailService.save(orderId.toString(), UUID.randomUUID().toString(), 1);
    assertFalse(result);
  }

  @Test
  void saveFailedInvalidQuantity() {
    UUID orderId = seedAndGetIdOfOrder();
    UUID productId = seedAndGetIdOfProduct();
    boolean result = orderDetailService.save(orderId.toString(), productId.toString(), 0);
    assertFalse(result);
  }

  @Test
  void findAllPopulated() {
    UUID orderId = seedAndGetIdOfOrder();
    UUID productId = seedAndGetIdOfProduct();
    orderDetailService.save(orderId.toString(), productId.toString(), 1);
    assertFalse(orderDetailService.findAll().isEmpty());
  }

  @Test
  void findAllUnpopulated() {
    assertTrue(orderDetailService.findAll().isEmpty());
  }

  @Test
  void findByIdSuccess() {
    UUID orderId = seedAndGetIdOfOrder();
    UUID productId = seedAndGetIdOfProduct();
    orderDetailService.save(orderId.toString(), productId.toString(), 1);
    UUID orderDetailId = orderDetailService.findAll().get(0).getId();
    assertTrue(orderDetailService.findById(orderDetailId.toString()).isPresent());
  }

  @Test
  void findByIdFailedInvalidId() {
    assertFalse(orderDetailService.findById("invalid-uuid").isPresent());
  }

  @Test
  void findByIdFailedNonexistentId() {
    assertFalse(orderDetailService.findById(UUID.randomUUID().toString()).isPresent());
  }

  @Test
  void findByOrderIdSuccess() {
    UUID orderId = seedAndGetIdOfOrder();
    UUID productId = seedAndGetIdOfProduct();
    orderDetailService.save(orderId.toString(), productId.toString(), 1);
    assertFalse(orderDetailService.findByOrderId(orderId.toString()).isEmpty());
  }

  @Test
  void findByOrderIdFailedInvalidId() {
    assertTrue(orderDetailService.findByOrderId("invalid-uuid").isEmpty());
  }

  @Test
  void findByOrderIdFailedNonexistentId() {
    assertTrue(orderDetailService.findByOrderId(UUID.randomUUID().toString()).isEmpty());
  }

  @Test
  void existByIdSuccess() {
    UUID orderId = seedAndGetIdOfOrder();
    UUID productId = seedAndGetIdOfProduct();
    orderDetailService.save(orderId.toString(), productId.toString(), 1);
    UUID orderDetailId = orderDetailService.findAll().get(0).getId();
    assertTrue(orderDetailService.existById(orderDetailId.toString()));
  }

  @Test
  void existByIdFailedInvalidId() {
    assertFalse(orderDetailService.existById("invalid-uuid"));
  }

  @Test
  void existByIdFailedNonexistentId() {
    assertFalse(orderDetailService.existById(UUID.randomUUID().toString()));
  }
}