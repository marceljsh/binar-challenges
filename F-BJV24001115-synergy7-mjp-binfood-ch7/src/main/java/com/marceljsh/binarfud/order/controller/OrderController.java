package com.marceljsh.binarfud.order.controller;

import com.marceljsh.binarfud.order.dto.OrderResponse;
import com.marceljsh.binarfud.order.service.OrderService;
import com.marceljsh.binarfud.order.dto.OrderAddRequest;
import com.marceljsh.binarfud.reporting.dto.ReceiptResponse;
import com.marceljsh.binarfud.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final Logger log = LoggerFactory.getLogger(OrderController.class);

  private final OrderService orderService;

  private final ReportingService reportingService;

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderResponse> add(@RequestBody OrderAddRequest request) {
    log.info("Received add order request: {}", request);

    OrderResponse body = orderService.save(request);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @GetMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderResponse> get(@PathVariable("id") String id) {
    log.info("Received get order request: {}", id);

    UUID orderId = UUID.fromString(id);

    OrderResponse body = orderService.get(orderId);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
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

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @GetMapping(
    value = "/{id}/receipt",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ReceiptResponse> getReceipt(@PathVariable("id") String id) {
    log.info("Received get order receipt request: {}", id);

    UUID orderId = UUID.fromString(id);
    ReceiptResponse body = reportingService.generateReceipt(orderId);

    return ResponseEntity.ok(body);
  }

}
