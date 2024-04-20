package com.marceljsh.binfood.model.repository.spec;

import com.marceljsh.binfood.model.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  boolean save(User user);

  Optional<User> findById(UUID id);

  List<User> findAll();

  Optional<User> findFirstByEmail(String name);

  List<User> findByName(String name);

  void deleteById(UUID id);

  void clear();

  int size();
}
