package com.marceljsh.binarfud.repository;

import com.marceljsh.binarfud.model.Merchant;
import com.marceljsh.binarfud.payload.response.MerchantResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

  @Transactional
  default void softDelete(UUID id) {
    Merchant merchant = findById(id).orElseThrow(() -> new IllegalArgumentException("merchant not found"));
    merchant.softDelete();
    save(merchant);
  }

  boolean existsByNameAndLocation(String name, String location);

  @Query(value = "CALL update_merchant_open(:id, :status)", nativeQuery = true)
  void updateStatus(@Param("id") UUID id, @Param("status") boolean open);

  @Query(value = "SELECT * FROM update_merchant_info(:id, :name, :location)", nativeQuery = true)
  MerchantResponse updateInfo(@Param("id") UUID id, @Param("name") String name, @Param("location") String location);
}
