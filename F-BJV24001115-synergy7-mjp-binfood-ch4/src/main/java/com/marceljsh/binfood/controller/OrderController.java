package com.marceljsh.binfood.controller;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.response.ApiResponse;
import com.marceljsh.binfood.payload.response.OrderResponse;
import com.marceljsh.binfood.service.spec.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<OrderResponse> add(User user) {
    return ApiResponse.<OrderResponse>builder().data(orderService.add(user)).build();
  }

  @GetMapping(path = "/{order-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<OrderResponse> get(User user, @PathVariable("order-id") String id) {
    return ApiResponse.<OrderResponse>builder().data(orderService.findById(id)).build();
  }

  @DeleteMapping(path = "/{order-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> remove(User user, @PathVariable("order-id") String id) {
    orderService.remove(id);
    return ApiResponse.<String>builder().data("OK").build();
  }

  @PatchMapping(path = "/{order-id}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> complete(User user, @PathVariable("order-id") String id) {
    orderService.complete(id);
    return ApiResponse.<String>builder().data("OK").build();
  }
}
