package com.marceljsh.binfood.controller;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.OrderDetailAddRequest;
import com.marceljsh.binfood.payload.response.ApiResponse;
import com.marceljsh.binfood.payload.response.OrderDetailResponse;
import com.marceljsh.binfood.service.spec.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-details")
public class OrderDetailController {

  private final OrderDetailService orderDetailService;

  @Autowired
  public OrderDetailController(OrderDetailService orderDetailService) {
    this.orderDetailService = orderDetailService;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<OrderDetailResponse> add(User user, OrderDetailAddRequest request) {
    return ApiResponse.<OrderDetailResponse>builder().data(orderDetailService.add(request)).build();
  }

  @GetMapping(path = "/{order-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<List<OrderDetailResponse>> findByOrderId(User user, @PathVariable("order-id") String orderId) {
    List<OrderDetailResponse> result = orderDetailService.findByOrderId(orderId);
    return ApiResponse.<List<OrderDetailResponse>>builder().data(result).build();
  }
}
