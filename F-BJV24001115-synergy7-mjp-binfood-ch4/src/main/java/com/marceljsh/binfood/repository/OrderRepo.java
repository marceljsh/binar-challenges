package com.marceljsh.binfood.repository;

import com.marceljsh.binfood.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

  @Transactional
  default void softDelete(UUID id) {
    Order order = findById(id).orElseThrow(() -> new IllegalArgumentException("order not found"));
    order.softDelete();
    save(order);
  }

  @Query(value = "CALL update_order_completed(:id, :completed)", nativeQuery = true)
  void updateStatus(@Param("id") UUID id, @Param("completed") boolean completed);
}
