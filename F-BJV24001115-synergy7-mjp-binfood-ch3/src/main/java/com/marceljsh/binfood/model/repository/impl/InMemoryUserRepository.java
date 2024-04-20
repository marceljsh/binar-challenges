package com.marceljsh.binfood.model.repository.impl;

import com.marceljsh.binfood.model.entity.User;
import com.marceljsh.binfood.model.repository.spec.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

  private static InMemoryUserRepository instance;

  private final Map<UUID, User> users;

  private InMemoryUserRepository() {
    users = new HashMap<>();
  }

  public static InMemoryUserRepository getInstance() {
    if (instance == null) {
      instance = new InMemoryUserRepository();
    }
    return instance;
  }

  @Override
  public boolean save(User user) {
    if (user.getId() == null) {
      user.setId(UUID.randomUUID());
    }
    return users.put(user.getId(), user) == null;
  }

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(users.get(id));
  }

  @Override
  public List<User> findAll() {
    return List.copyOf(users.values());
  }

  @Override
  public Optional<User> findFirstByEmail(String email) {
    return users.values().stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst();
  }

  @Override
  public List<User> findByName(String name) {
    return users.values().stream()
        .filter(user -> user.getName().contains(name))
        .toList();
  }

  @Override
  public void deleteById(UUID id) {
    users.remove(id);
  }

  @Override
  public void clear() {
    users.clear();
  }

  @Override
  public int size() {
    return users.size();
  }
}
