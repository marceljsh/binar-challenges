package com.marceljsh.binfood.repository;

import com.marceljsh.binfood.model.Merchant;
import com.marceljsh.binfood.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

  @Transactional
  default void softDelete(UUID id) {
    Product product = findById(id).orElseThrow(() -> new IllegalArgumentException("product not found"));
    product.softDelete();
    save(product);
  }

  boolean existsByNameAndPriceAndSeller(String name, long price, Merchant seller);

  @Query("SELECT p FROM Product p WHERE p.seller.open = true")
  List<Product> findProductsOfOpenMerchants();
}
