package com.marceljsh.binfood.service.impl;

import com.marceljsh.binfood.model.Order;
import com.marceljsh.binfood.model.OrderDetail;
import com.marceljsh.binfood.model.Product;
import com.marceljsh.binfood.payload.request.OrderDetailAddRequest;
import com.marceljsh.binfood.payload.response.OrderDetailResponse;
import com.marceljsh.binfood.payload.response.OrderResponse;
import com.marceljsh.binfood.payload.response.ProductResponse;
import com.marceljsh.binfood.repository.OrderDetailRepo;
import com.marceljsh.binfood.repository.OrderRepo;
import com.marceljsh.binfood.repository.ProductRepo;
import com.marceljsh.binfood.service.spec.OrderDetailService;
import com.marceljsh.binfood.service.spec.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class DefaultOrderDetailService implements OrderDetailService {

  private final OrderDetailRepo orderDetailRepo;

  private final OrderRepo orderRepo;

  private final ProductRepo productRepo;

  private final ValidationService validationService;

  @Autowired
  public DefaultOrderDetailService(OrderDetailRepo orderDetailRepo, OrderRepo orderRepo, ProductRepo productRepo, ValidationService validationService) {
    this.orderDetailRepo = orderDetailRepo;
    this.orderRepo = orderRepo;
    this.productRepo = productRepo;
    this.validationService = validationService;
  }

  private OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail) {
    return OrderDetailResponse.builder()
      .id(orderDetail.getId())
      .quantity(orderDetail.getQuantity())
      .order(toOrderResponse(orderDetail.getOrder()))
      .product(toProductResponse(orderDetail.getProduct()))
      .build();
  }

  private OrderResponse toOrderResponse(Order order) {
    return OrderResponse.builder()
      .id(order.getId())
      .userId(order.getUser().getId())
      .createdAt(order.getCreatedAt())
      .updatedAt(order.getUpdatedAt())
      .build();
  }

  private ProductResponse toProductResponse(Product product) {
    return ProductResponse.builder()
      .id(product.getId())
      .name(product.getName())
      .price(product.getPrice())
      .seller(product.getSeller().getName())
      .build();
  }

  @Transactional
  @Override
  public OrderDetailResponse add(OrderDetailAddRequest request) {
    validationService.validate(request);

    UUID orderId = UUID.fromString(request.getOrderId());
    Order order = orderRepo.findById(orderId).orElseThrow(() -> new IllegalArgumentException("order not found"));

    UUID productId = UUID.fromString(request.getProductId());
    Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("product not found"));

    OrderDetail orderDetail = OrderDetail.builder()
      .order(order)
      .product(product)
      .quantity(request.getQuantity())
      .build();

    OrderDetail other = orderDetailRepo.save(orderDetail);

    if (!orderDetail.equals(other)) {
      throw new IllegalArgumentException("failed to save order_detail");
    }

    return toOrderDetailResponse(orderDetail);
  }

  @Transactional(readOnly = true)
  @Override
  public List<OrderDetailResponse> findByOrderId(String orderIdString) {
    if (!validationService.isValidUUID(orderIdString)) {
      throw new IllegalArgumentException("invalid order_id");
    }

    UUID orderId = UUID.fromString(orderIdString);
    if (!orderRepo.existsById(orderId)) {
      throw new IllegalArgumentException("order not found");
    }

    List<OrderDetail> orderDetail = orderDetailRepo.findByOrderId(orderId);

    return orderDetail.stream().map(this::toOrderDetailResponse).toList();
  }
}
