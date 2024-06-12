package com.marceljsh.binarfud.repository;

import com.marceljsh.binarfud.model.OrderDetail;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

  @Transactional
  default void softDelete(UUID id) {
    OrderDetail orderDetail = findById(id).orElse(null);

    if (orderDetail != null) {
      orderDetail.onDelete();
      save(orderDetail);
    }
  }

  @Transactional
  default void restore(UUID id) {
    OrderDetail orderDetail = findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.ORDER_DETAIL_NOT_FOUND));

    orderDetail.onRestore();
    save(orderDetail);
  }

  List<OrderDetail> findByOrderId(UUID orderId);
}
