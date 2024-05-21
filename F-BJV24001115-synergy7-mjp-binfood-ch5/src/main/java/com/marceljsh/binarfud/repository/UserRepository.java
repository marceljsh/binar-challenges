package com.marceljsh.binarfud.repository;

import com.marceljsh.binarfud.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  @Transactional
  default void softDelete(UUID id) {
    User user = findById(id).orElseThrow(() -> new EntityNotFoundException("no such user"));
    user.softDelete();
    save(user);
  }

  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  Optional<User> findFirstByToken(String token);
  Optional<User> findFirstByUsername(String username);
}
