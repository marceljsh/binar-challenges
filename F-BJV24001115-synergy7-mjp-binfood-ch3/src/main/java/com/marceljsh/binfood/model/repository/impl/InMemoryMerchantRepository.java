package com.marceljsh.binfood.model.repository.impl;

import com.marceljsh.binfood.model.entity.Merchant;
import com.marceljsh.binfood.model.repository.spec.MerchantRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryMerchantRepository implements MerchantRepository {

  private static InMemoryMerchantRepository instance;

  private final Map<UUID, Merchant> merchants;

  private InMemoryMerchantRepository() {
    merchants = new HashMap<>();
  }

  public static InMemoryMerchantRepository getInstance() {
    if (instance == null) {
      instance = new InMemoryMerchantRepository();
    }
    return instance;
  }

  @Override
  public boolean save(Merchant merchant) {
    if (merchant.getId() == null) {
      merchant.setId(UUID.randomUUID());
    }
    return merchants.put(merchant.getId(), merchant) == null;
  }

  @Override
  public Optional<Merchant> findById(UUID id) {
    return Optional.ofNullable(merchants.get(id));
  }

  @Override
  public List<Merchant> findAll() {
    return List.copyOf(merchants.values());
  }

  @Override
  public List<Merchant> findByName(String name) {
    return merchants.values().stream()
        .filter(merchant -> merchant.getName().contains(name))
        .toList();
  }

  @Override
  public List<Merchant> findByLocation(String location) {
    return merchants.values().stream()
        .filter(merchant -> merchant.getLocation().contains(location))
        .toList();
  }

  @Override
  public void deleteById(UUID id) {
    merchants.remove(id);
  }

  @Override
  public void deleteAll() {
    merchants.clear();
  }

  @Override
  public int size() {
    return merchants.size();
  }
}
