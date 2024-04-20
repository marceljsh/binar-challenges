package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateMerchantRequest;
import com.marceljsh.binfood.model.entity.Merchant;
import com.marceljsh.binfood.model.repository.impl.InMemoryMerchantRepository;
import com.marceljsh.binfood.model.repository.spec.MerchantRepository;
import com.marceljsh.binfood.util.Verifier;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MerchantService {

  private static MerchantService instance;

  private final MerchantRepository merchantRepository;

  private final Verifier verifier;

  private MerchantService() {
    merchantRepository = InMemoryMerchantRepository.getInstance();
    verifier = Verifier.getInstance();
  }

  public static MerchantService getInstance() {
    if (instance == null) {
      instance = new MerchantService();
    }
    return instance;
  }

  public boolean save(CreateMerchantRequest request) {
    Set<ConstraintViolation<CreateMerchantRequest>> violations = verifier.verify(request);
    if (!violations.isEmpty()) {
      return false;
    }

    return merchantRepository.save(new Merchant(
      null,
      request.getName(),
      request.getLocation(),
      true
    ));
  }

  public Optional<Merchant> findById(String id) {
    if (!verifier.isValidUUID(id)) {
      return Optional.empty();
    }
    return merchantRepository.findById(UUID.fromString(id));
  }

  public List<Merchant> findAll() {
    return merchantRepository.findAll();
  }

  public List<Merchant> findByName(String name) {
    return merchantRepository.findByName(name);
  }

  public List<Merchant> findByLocation(String location) {
    return merchantRepository.findByLocation(location);
  }

  // TODO: update entity

  public void deleteById(String id) {
    if (verifier.isValidUUID(id)) {
      merchantRepository.deleteById(UUID.fromString(id));
    }
  }

  public int size() {
    return merchantRepository.size();
  }

  public void clear() {
    merchantRepository.deleteAll();
  }
}
