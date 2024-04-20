package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateUserRequest;
import com.marceljsh.binfood.model.entity.User;
import com.marceljsh.binfood.model.repository.impl.InMemoryUserRepository;
import com.marceljsh.binfood.model.repository.spec.UserRepository;
import com.marceljsh.binfood.util.Verifier;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class UserService {

  private static UserService instance;

  private final UserRepository userRepository;

  private final Verifier verifier;

  private UserService() {
    userRepository = InMemoryUserRepository.getInstance();
    verifier = Verifier.getInstance();
  }

  public static UserService getInstance() {
    if (instance == null) {
      instance = new UserService();
    }
    return instance;
  }

  public boolean save(CreateUserRequest request) {
    Set<ConstraintViolation<CreateUserRequest>> violations = verifier.verify(request);
    if (!violations.isEmpty()) {
      return false;
    }

    return userRepository.save(new User(
      null,
      request.getName(),
      request.getEmail(),
      request.getPassword()
    ));
  }

  public Optional<User> findById(String id) {
    if (!verifier.isValidUUID(id)) {
      return Optional.empty();
    }

    UUID uuid = UUID.fromString(id);
    return userRepository.findById(uuid);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findFirstByEmail(String email) {
    return userRepository.findFirstByEmail(email);
  }

  public List<User> findByName(String name) {
    return userRepository.findByName(name);
  }

  // TODO: update entity

  public void deleteById(String id) {
    if (!verifier.isValidUUID(id)) {
      return;
    }

    UUID uuid = UUID.fromString(id);
    userRepository.deleteById(uuid);
  }

  public void clear() {
    userRepository.clear();
  }

  public int size() {
    return userRepository.size();
  }
}
