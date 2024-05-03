package com.marceljsh.binfood.service.impl;

import com.marceljsh.binfood.model.Merchant;
import com.marceljsh.binfood.model.Product;
import com.marceljsh.binfood.payload.request.ProductAddRequest;
import com.marceljsh.binfood.payload.request.ProductSearchRequest;
import com.marceljsh.binfood.payload.request.ProductUpdateRequest;
import com.marceljsh.binfood.payload.response.ProductResponse;
import com.marceljsh.binfood.repository.MerchantRepo;
import com.marceljsh.binfood.repository.ProductRepo;
import com.marceljsh.binfood.service.spec.ProductService;
import com.marceljsh.binfood.service.spec.ValidationService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class DefaultProductService implements ProductService {

  private final ProductRepo productRepo;

  private final MerchantRepo merchantRepo;

  private final ValidationService validationService;

  @Autowired
  public DefaultProductService(ProductRepo productRepo, MerchantRepo merchantRepo,
      ValidationService validationService) {
    this.productRepo = productRepo;
    this.merchantRepo = merchantRepo;
    this.validationService = validationService;
  }

  private ProductResponse toProductResponse(Product product) {
    return ProductResponse.builder()
      .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .seller(product.getSeller().getName())
        .build();
  }

  @Transactional
  @Override
  public ProductResponse add(ProductAddRequest request) {
    validationService.validate(request);

    UUID sellerId = UUID.fromString(request.getSellerId());
    Merchant seller = merchantRepo.findById(sellerId)
        .orElseThrow(() -> new IllegalArgumentException("merchant not found"));

    if (productRepo.existsByNameAndPriceAndSeller(request.getName(), request.getPrice(), seller)) {
      throw new EntityExistsException("product already exist");
    }

    Product product = Product.builder()
      .name(request.getName())
      .price(request.getPrice())
      .seller(seller)
      .build();

    Product other = productRepo.save(product);
    if (!product.equals(other)) {
      throw new TransactionSystemException("failed to save product");
    }

    return toProductResponse(product);
  }

  @Transactional
  @Override
  public void remove(String idString) {
    if (!validationService.isValidUUID(idString)) {
      throw new IllegalArgumentException("invalid product_id");
    }

    UUID id = UUID.fromString(idString);
    productRepo.softDelete(id);
  }

  @Transactional(readOnly = true)
  @Override
  public ProductResponse findById(String idString) {
    if (!validationService.isValidUUID(idString)) {
      throw new IllegalArgumentException("invalid product_id");
    }

    UUID id = UUID.fromString(idString);
    Product product = productRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("product not found"));

    return toProductResponse(product);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<ProductResponse> search(ProductSearchRequest request) {
    Specification<Product> specification = (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getName())) {
        log.debug("searching by name: {}", request.getName());
        predicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("name")), "%" + request.getName() + "%"
        ));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    };

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Product> products = productRepo.findAll(specification, pageable);
    List<ProductResponse> responses = products.map(this::toProductResponse).toList();

    return new PageImpl<>(responses, pageable, products.getTotalElements());
  }

  @Transactional
  @Override
  public ProductResponse update(ProductUpdateRequest request) {
    validationService.validate(request);

    UUID id = UUID.fromString(request.getId());
    Product product = productRepo.findById(id)
      .orElseThrow(() -> {
        log.error("product not found");
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
      });

    product.setName(request.getName());
    product.setPrice(request.getPrice());

    Product other = productRepo.save(product);
    if (!product.equals(other)) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to save product");
    }

    return toProductResponse(product);
  }
}
