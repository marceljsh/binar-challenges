package com.marceljsh.binarfud.repository;

import com.marceljsh.binarfud.model.Order;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

  @Transactional
  default void softDelete(UUID id) {
    Order order = findById(id).orElse(null);

    if (order != null) {
      order.onDelete();
      save(order);
    }
  }

  @Transactional
  default void restore(UUID id) {
    Order order = findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.ORDER_NOT_FOUND));

    order.onRestore();
    save(order);
  }
}
