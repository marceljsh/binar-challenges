package com.marceljsh.binfood.repository;

import com.marceljsh.binfood.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, UUID> {

  @Transactional
  default void softDelete(UUID id) {
    OrderDetail orderDetail = findById(id).orElseThrow(() -> new IllegalArgumentException("order detail not found"));
    orderDetail.softDelete();
    save(orderDetail);
  }

  List<OrderDetail> findByOrderId(UUID orderId);
}
