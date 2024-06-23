package com.marceljsh.binarfud.order.controller;

import com.marceljsh.binarfud.order.dto.OrderResponse;
import com.marceljsh.binarfud.order.service.OrderService;
import com.marceljsh.binarfud.order.dto.OrderAddRequest;
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

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final Logger log = LoggerFactory.getLogger(OrderController.class);

  @Autowired
  private OrderService orderService;

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderResponse> add(@RequestBody OrderAddRequest request) {
    log.info("Received add order request: {}", request);

    OrderResponse response = orderService.save(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderResponse> get(@PathVariable("id") String id) {
    log.info("Received get order request: {}", id);

    UUID orderId = UUID.fromString(id);

    OrderResponse response = orderService.get(orderId);

    return ResponseEntity.ok(response);
  }

  @PatchMapping(
    value = "/{id}/complete",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderResponse> complete(@PathVariable("id") String id) {
    log.info("Received complete order request: {}", id);

    UUID orderId = UUID.fromString(id);

    orderService.complete(orderId);

    return ResponseEntity.ok().build();
  }

}
