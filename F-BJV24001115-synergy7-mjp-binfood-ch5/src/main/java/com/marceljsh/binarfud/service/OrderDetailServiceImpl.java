package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.model.Order;
import com.marceljsh.binarfud.model.OrderDetail;
import com.marceljsh.binarfud.model.Product;
import com.marceljsh.binarfud.payload.request.OrderDetailAddRequest;
import com.marceljsh.binarfud.payload.response.OrderDetailResponse;
import com.marceljsh.binarfud.repository.OrderDetailRepository;
import com.marceljsh.binarfud.repository.OrderRepository;
import com.marceljsh.binarfud.repository.ProductRepository;
import com.marceljsh.binarfud.service.spec.OrderDetailService;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class OrderDetailServiceImpl implements OrderDetailService {

  @Autowired
  private OrderDetailRepository orderDetailRepo;

  @Autowired
  private OrderRepository orderRepo;

  @Autowired
  private ProductRepository productRepo;

  @Transactional
  @Override
  public OrderDetailResponse save(OrderDetailAddRequest request) {
    UUID orderId = UUID.fromString(request.getOrderId());
    Order order = orderRepo.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.PRODUCT_NOT_FOUND));

    UUID productId = UUID.fromString(request.getProductId());
    Product product = productRepo.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.PRODUCT_NOT_FOUND));

    BigDecimal quantity = BigDecimal.valueOf(request.getQuantity());

    OrderDetail orderDetail = OrderDetail.builder()
      .order(order)
      .product(product)
      .quantity(request.getQuantity())
      .totalPrice(product.getPrice().multiply(quantity))
      .build();

    OrderDetail other = orderDetailRepo.save(orderDetail);
    if (!orderDetail.equals(other)) {
      throw new RuntimeException("failed to create order detail");
    }

    return OrderDetailResponse.of(orderDetail);
  }

  @Transactional
  @Override
  public void archive(UUID id) {
    orderDetailRepo.softDelete(id);
  }

  @Transactional
  @Override
  public void restore(UUID id) {
    orderDetailRepo.restore(id);
  }

  @Transactional
  @Override
  public void remove(UUID id) {
    orderDetailRepo.deleteById(id);
  }

  @Transactional
  @Override
  public OrderDetailResponse get(UUID id) {
    OrderDetail orderDetail = orderDetailRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.ORDER_DETAIL_NOT_FOUND));

    return OrderDetailResponse.of(orderDetail);
  }

  @Transactional
  @Override
  public List<OrderDetailResponse> getByOrderId(UUID orderId) {
    return orderDetailRepo.findByOrderId(orderId)
      .stream()
      .map(OrderDetailResponse::of)
      .toList();
  }
}
