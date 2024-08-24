package com.marceljsh.binfood.repository;

import com.marceljsh.binfood.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface MerchantRepo extends JpaRepository<Merchant, UUID>, JpaSpecificationExecutor<Merchant> {

  @Transactional
  default void softDelete(UUID id) {
    Merchant merchant = findById(id).orElseThrow(() -> new IllegalArgumentException("merchant not found"));
    merchant.softDelete();
    save(merchant);
  }

  boolean existsByNameAndLocation(String name, String location);

  @Query(value = "CALL update_merchant_open(:id, :status)", nativeQuery = true)
  void updateStatus(@Param("id") UUID id, @Param("status") boolean status);
}
