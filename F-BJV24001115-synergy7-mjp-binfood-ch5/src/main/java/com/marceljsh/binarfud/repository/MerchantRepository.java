package com.marceljsh.binarfud.repository;

import com.marceljsh.binarfud.model.Merchant;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID>, JpaSpecificationExecutor<Merchant> {

  // TODO: find a better way to soft delete, maybe pass DateTime.Now() to SP
  @Transactional
  default void softDelete(UUID id) {
    Merchant merchant = findById(id).orElse(null);

    if (merchant != null) {
      merchant.onDelete();
      save(merchant);
    }
  }

  @Transactional
  default void restore(UUID id) {
    Merchant merchant = findById(id)
        .orElseThrow(() -> new EntityNotFoundException("merchant not found"));

    merchant.onRestore();
    save(merchant);
  }

  boolean existsByNameAndLocation(String name, String location);

  @Transactional(readOnly = true)
  @Query(value = "SELECT open FROM tbl_merchants WHERE id = :id", nativeQuery = true)
  boolean isOpenById(@Param("id") UUID id);

  @Modifying
  @Transactional
  @Query(value = "CALL update_merchant_open(:id, :status)", nativeQuery = true)
  void updateStatus(@Param("id") UUID id, @Param("status") boolean open);

  @Transactional
  @Query(value = "SELECT * FROM update_merchant_info(:id, :name, :location)", nativeQuery = true)
  Merchant updateInfo(@Param("id") UUID id, @Param("name") String name, @Param("location") String location);
}
