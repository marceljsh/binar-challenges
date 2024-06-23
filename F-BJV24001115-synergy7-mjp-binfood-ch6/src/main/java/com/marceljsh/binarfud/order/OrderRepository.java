package com.marceljsh.binarfud.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

  @Modifying
  @Query("UPDATE Order o SET o.completed = true, o.updatedAt = CURRENT_TIMESTAMP WHERE o.id = :id")
  void completeById(UUID id);

}
