package com.marceljsh.binarfud.product.service;

import com.marceljsh.binarfud.app.util.Constants;
import com.marceljsh.binarfud.app.exception.PageNotFoundException;
import com.marceljsh.binarfud.merchant.model.Merchant;
import com.marceljsh.binarfud.merchant.repository.MerchantRepository;
import com.marceljsh.binarfud.product.dto.ProductUpdateInfoRequest;
import com.marceljsh.binarfud.product.dto.ProductAddRequest;
import com.marceljsh.binarfud.product.dto.ProductResponse;
import com.marceljsh.binarfud.product.dto.ProductSearchRequest;
import com.marceljsh.binarfud.product.model.Product;
import com.marceljsh.binarfud.product.repository.ProductRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ProductServiceImpl implements ProductService {

  private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

  @Autowired
  private ProductRepository productRepo;

  @Autowired
  private MerchantRepository merchantRepo;

  @Override
  @Transactional
  public ProductResponse save(ProductAddRequest request) {
    log.trace("Saving product={} for merchant sellerId={}",
        request.getName(), request.getSellerId());

    Merchant merchant = merchantRepo.findById(request.getSellerId()).orElse(null);
    if (merchant == null) {
      log.error("Merchant not found with id={}", request.getSellerId());
      throw new EntityNotFoundException(Constants.MSG_MERCHANT_NOT_FOUND);
    }

    Product product = Product.builder()
        .name(request.getName())
        .price(request.getPrice())
        .stock(request.getStock())
        .seller(merchant)
        .build();

    log.info("Saving new product={} for merchant {} in {}",
        product.getName(), merchant.getName(), merchant.getLocation());

    return ProductResponse.from(productRepo.save(product));
  }

  @Override
  @Transactional
  public void archive(UUID id) {
    log.info("Archiving product with id: {}", id);

    if (productRepo.existsById(id)) {
      log.trace("Proceeding to archive found product with id: {}", id);
      productRepo.archiveById(id);
    }
  }

  @Override
  @Transactional
  public void restore(UUID id) {
    log.info("Restoring product with id: {}", id);

    if (productRepo.existsById(id)) {
      log.trace("Proceeding to restore found product with id: {}", id);
      productRepo.restoreById(id);
    }
  }

  @Override
  @Transactional
  public void remove(UUID id) {
    log.info("Removing product with id: {}", id);

    if (productRepo.existsById(id)) {
      log.trace("Proceeding to remove found product with id: {}", id);
      productRepo.deleteById(id);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public ProductResponse get(UUID id) {
    log.info("Fetching product with id: {}", id);

    Product product = productRepo.findById(id).orElse(null);

    if (product == null) {
      log.error("Product with id: {} not found", id);
      throw new EntityNotFoundException(Constants.MSG_PRODUCT_NOT_FOUND);
    }

    Merchant seller = product.getSeller();
    log.info("Found product {} by {} in {}",
        product, seller.getName(), seller.getLocation());

    return ProductResponse.from(product);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductResponse> search(ProductSearchRequest request) {
    log.trace(
        "Searching for products: name={}, minPrice={}, maxPrice={}, seller={}, page={}, size={}",
        request.getName(),
        request.getMinPrice(),
        request.getMaxPrice(),
        request.getSeller(),
        request.getPage(),
        request.getSize());

    Specification<Product> specification = ((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getName())) {
        predicates.add(cb.like(
            cb.lower(root.get("name")),
            "%" + request.getName().toLowerCase() + "%"));
      }

      if (Objects.nonNull(request.getMinPrice())) {
        predicates.add(cb.greaterThanOrEqualTo(
            root.get("price"),
            request.getMinPrice()));
      }

      if (Objects.nonNull(request.getMaxPrice())) {
        predicates.add(cb.lessThanOrEqualTo(
            root.get("price"),
            request.getMaxPrice()));
      }

      if (Objects.nonNull(request.getSeller())) {
        predicates.add(cb.like(
            cb.lower(root.get("seller").get("name")),
            "%" + request.getSeller().toLowerCase() + "%"));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    });

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Product> products = productRepo.findAll(specification, pageable);

    if (products.getTotalElements() == 0) {
      log.info("Product search returned no results");
      return new PageImpl<>(List.of(), pageable, 0);
    }

    if (request.getPage() > products.getTotalPages() - 1) {
      String error = String.format(
          "page %d out of bounds (%d)",
          request.getPage() + 1,
          products.getTotalPages());

      log.error(error);
      throw new PageNotFoundException(error);
    }

    log.info("Found {} products", products.getTotalElements());

    List<ProductResponse> result = products.getContent()
        .stream()
        .map(ProductResponse::from)
        .toList();

    return new PageImpl<>(result, pageable, products.getTotalElements());
  }

  @Override
  @Transactional
  public ProductResponse updateInfo(ProductUpdateInfoRequest request) {
    log.info("Updating info for product: {}", request.getId());

    Product product = productRepo.findById(request.getId()).orElse(null);

    if (product == null) {
      log.error("Product with id: {} not found", request.getId());
      throw new EntityNotFoundException(Constants.MSG_PRODUCT_NOT_FOUND);
    }

    product.setName(request.getName());
    product.setPrice(request.getPrice());

    log.info("Updated product: {}", product);

    return ProductResponse.from(productRepo.save(product));
  }

}
