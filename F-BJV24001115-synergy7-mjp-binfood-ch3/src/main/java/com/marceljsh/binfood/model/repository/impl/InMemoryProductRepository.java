package com.marceljsh.binfood.model.repository.impl;

import com.marceljsh.binfood.model.entity.Product;
import com.marceljsh.binfood.model.repository.spec.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class InMemoryProductRepository implements ProductRepository {

  private static InMemoryProductRepository instance;

  private final Map<UUID, Product> products;

  private InMemoryProductRepository() {
    products = new HashMap<>();
  }

  public static InMemoryProductRepository getInstance() {
    if (instance == null) {
      instance = new InMemoryProductRepository();
    }
    return instance;
  }

  @Override
  public boolean save(Product item) {
    if (item.getId() == null) {
      item.setId(UUID.randomUUID());
    }
    return products.put(item.getId(), item) == null;
  }

  @Override
  public Optional<Product> findById(UUID id) {
    return Optional.ofNullable(products.get(id));
  }

  @Override
  public List<Product> findAll() {
    return List.copyOf(products.values());
  }

  @Override
  public Optional<Product> findFirstByName(String name) {
    return products.values().stream()
      .filter(item -> item.getName().equals(name))
      .findFirst();
  }

  @Override
  public void deleteById(UUID id) {
    products.remove(id);
  }

  @Override
  public int size() {
    return products.size();
  }

  @Override
  public void clear() {
    products.clear();
  }
}
