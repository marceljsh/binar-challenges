package com.marceljsh.binarfud.user.repositoy;

import com.marceljsh.binarfud.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

  @Modifying
  @Query("UPDATE User u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
  void deactivateById(@Param("id") UUID id);

  @Modifying
  @Query("UPDATE User u SET u.deletedAt = null WHERE u.id = :id")
  void restoreById(@Param("id") UUID id);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmailOrUsername(String email, String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

}
