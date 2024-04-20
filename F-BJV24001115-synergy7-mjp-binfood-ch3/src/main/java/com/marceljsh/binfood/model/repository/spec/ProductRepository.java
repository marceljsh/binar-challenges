package com.marceljsh.binfood.model.repository.spec;

import com.marceljsh.binfood.model.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

  boolean save(Product product);
  Optional<Product> findById(UUID id);
  List<Product> findAll();
  Optional<Product> findFirstByName(String name);
  void deleteById(UUID id);
  int size();
  void clear();
}
