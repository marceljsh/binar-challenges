package com.marceljsh.binarfud.orderdetail.repo;

import com.marceljsh.binarfud.orderdetail.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

  List<OrderDetail> findByOrderId(UUID orderId);

}
