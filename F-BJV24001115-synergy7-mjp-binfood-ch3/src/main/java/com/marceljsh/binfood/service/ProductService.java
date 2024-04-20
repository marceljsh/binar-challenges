package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateProductRequest;
import com.marceljsh.binfood.model.entity.Merchant;
import com.marceljsh.binfood.model.entity.Product;
import com.marceljsh.binfood.model.repository.impl.InMemoryMerchantRepository;
import com.marceljsh.binfood.model.repository.spec.MerchantRepository;
import com.marceljsh.binfood.model.repository.spec.ProductRepository;
import com.marceljsh.binfood.model.repository.impl.InMemoryProductRepository;
import com.marceljsh.binfood.util.Verifier;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ProductService {

  private static ProductService instance;

  private final ProductRepository productRepository;

  private final MerchantRepository merchantRepository;

  private final Verifier verifier;

  private ProductService() {
    productRepository = InMemoryProductRepository.getInstance();
    merchantRepository = InMemoryMerchantRepository.getInstance();
    verifier = Verifier.getInstance();
  }

  public static ProductService getInstance() {
    if (instance == null) {
      instance = new ProductService();
    }
    return instance;
  }

  public boolean save(CreateProductRequest request) {
    Set<ConstraintViolation<CreateProductRequest>> violations = verifier.verify(request);
    if (!violations.isEmpty()) {
      return false;
    }

    String merchantIdStr = request.getMerchantId();
    if (!verifier.isValidUUID(merchantIdStr)) {
      return false;
    }

    UUID merchantId = UUID.fromString(merchantIdStr);
    Merchant merchant = merchantRepository.findById(merchantId).orElse(null);
    if (merchant == null) {
      return false;
    }

    return productRepository.save(new Product(
      null,
      request.getName(),
      request.getPrice(),
      merchant
    ));
  }

  public Optional<Product> findById(String id) {
    if (!verifier.isValidUUID(id)) {
      return Optional.empty();
    }

    UUID uuid = UUID.fromString(id);
    return productRepository.findById(uuid);
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  public Optional<Product> findFirstByName(String name) {
    return productRepository.findFirstByName(name);
  }

  // TODO: update entity

  public void deleteById(String id) {
    if (verifier.isValidUUID(id)) {
      productRepository.deleteById(UUID.fromString(id));
    }
  }

  public int size() {
    return productRepository.size();
  }

  public void clear() {
    productRepository.clear();
  }
}
