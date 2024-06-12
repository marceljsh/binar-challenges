package com.marceljsh.binarfud.controller;

import com.marceljsh.binarfud.payload.request.OrderAddRequest;
import com.marceljsh.binarfud.payload.response.OrderResponse;
import com.marceljsh.binarfud.service.spec.OrderService;
import com.marceljsh.binarfud.util.Constants;
import com.marceljsh.binarfud.validation.annotation.ValidUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final Logger log = LoggerFactory.getLogger(OrderController.class);

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderResponse> add(@RequestBody OrderAddRequest request) {
    log.info("adding order for user {}", request.getOwnerId());

    OrderResponse response = orderService.save(request);

    log.info("order added: {}", response.getId());
    return ResponseEntity.ok(response);
  }

  @GetMapping(
    path = "/{order-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderResponse> get(@PathVariable("order-id") @ValidUUID String id) {
    log.info("getting order {}", id);

    UUID orderId = UUID.fromString(id);
    OrderResponse response = orderService.get(orderId);

    log.info("order retrieved: {}", response.getId());
    return ResponseEntity.ok(response);
  }

  @PatchMapping(
    path = "/{order-id}/complete",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> complete(@PathVariable("order-id") @ValidUUID String id) {
    log.info("completing order {}", id);

    UUID orderId = UUID.fromString(id);
    orderService.complete(orderId);

    log.info("order completed: {}", id);
    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }

  @PatchMapping(
    path = "/{order-id}/restore",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> restore(@PathVariable("order-id") @ValidUUID String id) {
    log.info("restoring order {}", id);

    UUID orderId = UUID.fromString(id);
    orderService.restore(orderId);

    log.info("order restored: {}", id);
    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }
}
