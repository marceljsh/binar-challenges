package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.exhandling.PageNotFoundException;
import com.marceljsh.binarfud.model.Merchant;
import com.marceljsh.binarfud.model.Product;
import com.marceljsh.binarfud.payload.request.ProductAddRequest;
import com.marceljsh.binarfud.payload.request.ProductSearchRequest;
import com.marceljsh.binarfud.payload.request.ProductUpdateRequest;
import com.marceljsh.binarfud.payload.response.ProductEagerResponse;
import com.marceljsh.binarfud.payload.response.ProductResponse;
import com.marceljsh.binarfud.repository.MerchantRepository;
import com.marceljsh.binarfud.repository.ProductRepository;
import com.marceljsh.binarfud.service.spec.ProductService;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepo;

  private final MerchantRepository merchantRepo;

  private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

  @Autowired
  public ProductServiceImpl(ProductRepository productRepo, MerchantRepository merchantRepo) {
    this.productRepo = productRepo;
    this.merchantRepo = merchantRepo;
  }

  @Transactional
  @Override
  public ProductResponse save(ProductAddRequest request) {
    UUID sellerId = UUID.fromString(request.getSellerId());
    Merchant seller = merchantRepo.findById(sellerId)
        .orElseThrow(() -> new EntityNotFoundException("seller not found"));

    if (productRepo.existsByNameAndPriceAndSeller(request.getName(), request.getPrice(), seller)) {
      throw new EntityExistsException("product already exists");
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

    return ProductResponse.of(product);
  }

  @Transactional
  @Override
  public void archive(UUID id) {
    productRepo.softDelete(id);
  }

  @Transactional
  @Override
  public void restore(UUID id) {
    productRepo.restore(id);
  }

  @Transactional
  @Override
  public void remove(UUID id) {
    productRepo.deleteById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public ProductResponse get(UUID id, boolean eager) {
    Product product = productRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.PRODUCT_NOT_FOUND));

    return eager ? ProductEagerResponse.of(product) : ProductResponse.of(product);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<ProductResponse> search(ProductSearchRequest request) {
    Specification<Product> specification = ((root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getName())) {
        predicates.add(criteriaBuilder.like(
            criteriaBuilder.lower(root.get("name")),
            "%" + request.getName().toLowerCase() + "%"));
      }

      if (Objects.nonNull(request.getMinPrice())) {
        predicates.add(criteriaBuilder.ge(root.get("price"), request.getMinPrice()));
      }

      if (Objects.nonNull(request.getMaxPrice())) {
        predicates.add(criteriaBuilder.le(root.get("price"), request.getMaxPrice()));
      }

      if (Objects.nonNull(request.getSellerId())) {
        predicates.add(criteriaBuilder.equal(root.get("seller").get("id"), request.getSellerId()));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    });

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Product> products = productRepo.findAll(specification, pageable);

    if (products.getTotalElements() == 0) {
      return new PageImpl<>(List.of(), pageable, 0);
    }

    if (request.getPage() > products.getTotalPages() - 1) {
      throw new PageNotFoundException(
          String.format("page %d out of bounds (%s)", request.getPage() + 1, products.getTotalPages()));
    }

    List<ProductResponse> result = products.getContent()
        .stream()
        .map(request.isEager() ? ProductEagerResponse::of : ProductResponse::of)
        .toList();

    return new PageImpl<>(result, pageable, products.getTotalElements());
  }

  @Transactional
  @Override
  public ProductResponse updateInfo(ProductUpdateRequest request) {
    UUID id = UUID.fromString(request.getId());
    log.info("product id {} is valid", request.getId());

    if (!productRepo.existsById(id)) {
      log.error("no product with id {}", request.getId());
      throw new EntityNotFoundException(Constants.Msg.PRODUCT_NOT_FOUND);
    }

    log.info("hitting productRepo");
    Product product = productRepo.updateInfo(id, request.getName(), request.getPrice());
    log.info("productRepo processed product {}", request.getId());

    return ProductResponse.of(product);
  }
}
