package com.marceljsh.binarfud.orderdetail.service;

import com.marceljsh.binarfud.app.util.Constants;
import com.marceljsh.binarfud.order.model.Order;
import com.marceljsh.binarfud.order.repository.OrderRepository;
import com.marceljsh.binarfud.orderdetail.dto.OrderDetailAddRequest;
import com.marceljsh.binarfud.orderdetail.dto.OrderDetailResponse;
import com.marceljsh.binarfud.orderdetail.model.OrderDetail;
import com.marceljsh.binarfud.orderdetail.repo.OrderDetailRepository;
import com.marceljsh.binarfud.product.model.Product;
import com.marceljsh.binarfud.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDetailServiceImpl implements OrderDetailService {

  private final Logger log = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

  @Autowired
  private OrderDetailRepository odRepo;

  @Autowired
  private OrderRepository orderRepo;

  @Autowired
  private ProductRepository productRepo;

  @Override
  @Transactional
  public OrderDetailResponse save(OrderDetailAddRequest request) {
    log.trace("Saving order detail for order id={} product={}",
        request.getOrderId(), request.getProductId());

    Order order = orderRepo.findById(request.getOrderId()).orElse(null);
    if (order == null) {
      log.error("Order not found with id={}", request.getOrderId());
      throw new EntityNotFoundException(Constants.MSG_ORDER_NOT_FOUND);
    }

    Product product = productRepo.findById(request.getProductId()).orElse(null);
    if (product == null) {
      log.error("Product not found with id={}", request.getProductId());
      throw new EntityNotFoundException(Constants.MSG_PRODUCT_NOT_FOUND);
    }

    BigDecimal totalPrice = product.getPrice()
        .multiply(BigDecimal.valueOf(request.getQuantity()));

    OrderDetail od = OrderDetail.builder()
        .order(order)
        .product(product)
        .quantity(request.getQuantity())
        .totalPrice(totalPrice)
        .build();

    log.info("Saving order detail for order={} with product={} x {} pcs",
        order.getId(), product.getName(), request.getQuantity());

    return OrderDetailResponse.from(odRepo.save(od));
  }

  @Override
  @Transactional(readOnly = true)
  public OrderDetailResponse get(UUID id) {
    log.trace("Fetching order detail with id: {}", id);

    OrderDetail od = odRepo.findById(id).orElse(null);
    if (od == null) {
      log.error("Order detail not found with id: {}", id);
      throw new EntityNotFoundException("Order detail not found");
    }

    log.info("Found order detail with id: {}", id);

    return OrderDetailResponse.from(od);
  }

  @Override
  @Transactional(readOnly = true)
  public Set<OrderDetailResponse> getByOrderId(UUID orderId) {
    log.trace("Fetching order details for order with id: {}", orderId);

    Order order = orderRepo.findById(orderId).orElse(null);
    if (order == null) {
      log.error("Order not found with id: {}", orderId);
      throw new EntityNotFoundException(Constants.MSG_ORDER_NOT_FOUND);
    }

    log.info("Found order with id: {}", orderId);

    Set<OrderDetail> result = odRepo.findByOrderId(orderId);

    return result.stream()
        .map(OrderDetailResponse::from)
        .collect(Collectors.toSet());
  }

}
