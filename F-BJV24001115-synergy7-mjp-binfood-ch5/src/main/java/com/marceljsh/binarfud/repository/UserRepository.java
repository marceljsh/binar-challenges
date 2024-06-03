package com.marceljsh.binarfud.repository;

import com.marceljsh.binarfud.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

  @Transactional
  default void softDelete(UUID id) {
    User user = findById(id).orElse(null);

    if (user != null) {
      user.onDelete();
      save(user);
    }
  }

  @Transactional
  default void restore(UUID id) {
    User user = findById(id)
        .orElseThrow(() -> new EntityNotFoundException("user not found"));

    user.onRestore();
    save(user);
  }

  @Transactional
  @Query(value = "SELECT * FROM update_user_info(:id, :username)", nativeQuery = true)
  User updateInfo(@Param("id") UUID id, @Param("username") String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
