package com.marceljsh.binarfud.orderdetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/order-details")
public class OrderDetailController {

  private final Logger log = LoggerFactory.getLogger(OrderDetailController.class);

  @Autowired
  private OrderDetailService odService;

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderDetailResponse> add(@RequestBody OrderDetailAddRequest request) {
    log.info("Received add order detail request: {}", request);

    OrderDetailResponse response = odService.save(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderDetailResponse> get(@PathVariable("id") String id) {
    log.info("Received get order detail request: {}", id);

    UUID odId = UUID.fromString(id);

    OrderDetailResponse response = odService.get(odId);

    return ResponseEntity.ok(response);
  }

  @GetMapping(
    value = "/by-order/{order-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Set<OrderDetailResponse>> getByOrderId(@PathVariable("order-id") String orderId) {
    log.info("Received get order detail by order id request: {}", orderId);

    UUID oId = UUID.fromString(orderId);

    Set<OrderDetailResponse> response = odService.getByOrderId(oId);

    return ResponseEntity.ok(response);
  }

}
