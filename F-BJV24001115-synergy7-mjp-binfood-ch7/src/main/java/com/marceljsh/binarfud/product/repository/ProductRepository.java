package com.marceljsh.binarfud.product.repository;

import com.marceljsh.binarfud.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

  @Modifying
  @Query("UPDATE Product p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
  void archiveById(UUID id);

  @Modifying
  @Query("UPDATE Product p SET p.deletedAt = null WHERE p.id = :id")
  void restoreById(UUID id);
}
