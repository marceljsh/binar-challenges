package com.marceljsh.binarfud.repository;

import com.marceljsh.binarfud.model.Merchant;
import com.marceljsh.binarfud.model.Product;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

  @Transactional
  default void softDelete(UUID id) {
    Product product = findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.PRODUCT_NOT_FOUND));

    product.onDelete();
    save(product);
  }

  @Transactional
  default void restore(UUID id) {
    Product product = findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.PRODUCT_NOT_FOUND));

    product.onRestore();
    save(product);
  }

  boolean existsByNameAndPriceAndSeller(String name, long price, Merchant seller);

  @Modifying
  @Transactional
  @Query(value = "SELECT * FROM update_product_info(:id, :name, :price)", nativeQuery = true)
  Product updateInfo(@Param("id") UUID id, @Param("name") String name, @Param("price") long price);
}
