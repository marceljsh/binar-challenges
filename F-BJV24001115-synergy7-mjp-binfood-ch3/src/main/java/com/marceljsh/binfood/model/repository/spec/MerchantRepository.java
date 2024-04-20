package com.marceljsh.binfood.model.repository.spec;

import com.marceljsh.binfood.model.entity.Merchant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository {

  boolean save(Merchant merchant);

  Optional<Merchant> findById(UUID id);

  List<Merchant> findAll();

  List<Merchant> findByName(String name);

  List<Merchant> findByLocation(String location);

  void deleteById(UUID id);

  void deleteAll();

  int size();
}
