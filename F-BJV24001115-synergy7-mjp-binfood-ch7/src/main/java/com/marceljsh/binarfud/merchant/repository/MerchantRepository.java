package com.marceljsh.binarfud.merchant.repository;

import com.marceljsh.binarfud.merchant.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID>, JpaSpecificationExecutor<Merchant> {

  boolean existsByNameAndLocation(String name, String location);

  @Modifying
  @Query("UPDATE Merchant m SET m.open = :newStatus WHERE m.id = :id")
  void updateOpenStatus(@Param("id") UUID id, @Param("newStatus") boolean newStatus);

  @Modifying
  @Query("UPDATE Merchant m SET m.deletedAt = CURRENT_TIMESTAMP WHERE m.id = :id")
  void deactivateById(@Param("id") UUID id);

  @Modifying
  @Query("UPDATE Merchant m SET m.deletedAt = null WHERE m.id = :id")
  void restoreById(@Param("id") UUID id);

}
