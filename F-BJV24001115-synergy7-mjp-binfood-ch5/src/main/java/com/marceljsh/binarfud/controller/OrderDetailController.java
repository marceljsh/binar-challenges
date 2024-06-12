package com.marceljsh.binarfud.controller;

import com.marceljsh.binarfud.payload.request.OrderDetailAddRequest;
import com.marceljsh.binarfud.payload.response.OrderDetailResponse;
import com.marceljsh.binarfud.service.spec.OrderDetailService;
import com.marceljsh.binarfud.util.Constants;
import com.marceljsh.binarfud.validation.annotation.ValidUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/order-details")
public class OrderDetailController {

  private final Logger log = LoggerFactory.getLogger(OrderDetailController.class);

  @Autowired
  private OrderDetailService orderDetailService;

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderDetailResponse> add(@RequestBody OrderDetailAddRequest request) {
    log.debug("adding order detail for order {} | product {}",
      request.getOrderId(), request.getProductId());

    OrderDetailResponse response = orderDetailService.save(request);

    log.info("order detail added: {}", response.getId());
    return ResponseEntity.ok(response);
  }

  @GetMapping(
    path = "/{order-detail-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<OrderDetailResponse> get(@ValidUUID @PathVariable("order-detail-id") String id) {
    log.debug("getting order detail {}", id);

    UUID orderDetailId = UUID.fromString(id);
    OrderDetailResponse response = orderDetailService.get(orderDetailId);

    log.info("order detail found: {}", response.getId());
    return ResponseEntity.ok(response);
  }

  @GetMapping(
    path = "/by-order/{order-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<OrderDetailResponse>> getByOrderId(@ValidUUID @PathVariable(value = "order-id") String id) {
    log.debug("getting order details for order {}", id);

    UUID orderId = UUID.fromString(id);
    List<OrderDetailResponse> responses = orderDetailService.getByOrderId(orderId);

    log.info("order details found: {}", responses.size());
    return ResponseEntity.ok(responses);
  }

  @DeleteMapping(
    path = "/{order-detail-id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> archive(@ValidUUID @PathVariable("order-detail-id") String id) {
    log.debug("deleting order detail {}", id);

    UUID orderDetailId = UUID.fromString(id);
    orderDetailService.archive(orderDetailId);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }
}
