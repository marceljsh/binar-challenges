package com.marceljsh.binarfud.orderdetail.controller;

import com.marceljsh.binarfud.orderdetail.dto.OrderDetailResponse;
import com.marceljsh.binarfud.orderdetail.service.OrderDetailService;
import com.marceljsh.binarfud.orderdetail.dto.OrderDetailAddRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-details")
public class OrderDetailController {

  private final Logger log = LoggerFactory.getLogger(OrderDetailController.class);

  private final OrderDetailService odService;

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderDetailResponse> add(@RequestBody OrderDetailAddRequest request) {
    log.info("Received add order detail request: {}", request);

    OrderDetailResponse body = odService.save(request);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @GetMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderDetailResponse> get(@PathVariable("id") String id) {
    log.info("Received get order detail request: {}", id);

    UUID odId = UUID.fromString(id);

    OrderDetailResponse body = odService.get(odId);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @GetMapping(
    value = "/by-order/{order-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> getByOrderId(@PathVariable("order-id") String orderId) {
    log.info("Received get order detail by order id request: {}", orderId);

    UUID oId = UUID.fromString(orderId);

    List<OrderDetailResponse> data = odService.getByOrderId(oId);

    Map<String, Object> body = Map.of("data", data);

    return ResponseEntity.ok(body);
  }

}
