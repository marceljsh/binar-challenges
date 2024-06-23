package com.marceljsh.binarfud.orderdetail.repo;

import com.marceljsh.binarfud.orderdetail.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

  Set<OrderDetail> findByOrderId(UUID orderId);

}
