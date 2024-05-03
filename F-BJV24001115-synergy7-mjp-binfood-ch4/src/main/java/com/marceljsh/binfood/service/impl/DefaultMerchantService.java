package com.marceljsh.binfood.service.impl;

import com.marceljsh.binfood.payload.request.MerchantAddRequest;
import com.marceljsh.binfood.payload.request.MerchantSearchRequest;
import com.marceljsh.binfood.payload.request.MerchantUpdateRequest;
import com.marceljsh.binfood.payload.response.MerchantResponse;
import com.marceljsh.binfood.model.Merchant;
import com.marceljsh.binfood.repository.MerchantRepo;
import com.marceljsh.binfood.service.spec.MerchantService;
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
public class DefaultMerchantService implements MerchantService {

  private final MerchantRepo merchantRepo;

  private final ValidationService validationService;

  @Autowired
  public DefaultMerchantService(MerchantRepo merchantRepo, ValidationService validationService) {
    this.merchantRepo = merchantRepo;
    this.validationService = validationService;
  }

  private MerchantResponse toMerchantResponse(Merchant merchant) {
    return MerchantResponse.builder()
        .id(merchant.getId())
        .name(merchant.getName())
        .location(merchant.getLocation())
        .open(merchant.isOpen())
        .createdAt(merchant.getCreatedAt())
        .updatedAt(merchant.getUpdatedAt())
        .build();
  }

  private void checkId(String idString) {
    if (!validationService.isValidUUID(idString)) {
      log.error("invalid merchant_id");
      throw new IllegalArgumentException("invalid merchant_id");
    }
  }

  @Transactional
  @Override
  public MerchantResponse save(MerchantAddRequest request) {
    validationService.validate(request);
    log.debug("request: {}", request);

    Merchant merchant = Merchant.builder()
        .name(request.getName())
        .location(request.getLocation())
        .open(false)
        .build();

    if (merchantRepo.existsByNameAndLocation(merchant.getName(), merchant.getLocation())) {
      log.error("merchant already exists");
      throw new EntityExistsException("merchant already exists");
    }

    Merchant other = merchantRepo.save(merchant);

    if (!merchant.equals(other)) {
      log.error("failed to save merchant");
      throw new TransactionSystemException("failed to save merchant");
    }

    log.info("merchant created");
    return toMerchantResponse(merchant);
  }

  @Override
  public void remove(String idString) {
    checkId(idString);

    UUID id = UUID.fromString(idString);

    log.info("deleting merchant with id: {}", idString);
    merchantRepo.deleteById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public MerchantResponse findById(String idString) {
    checkId(idString);

    UUID id = UUID.fromString(idString);
    Merchant merchant = merchantRepo.findById(id).orElseThrow(() -> {
      log.error("merchant not found");
      return new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found");
    });

    log.info("merchant found");
    return toMerchantResponse(merchant);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<MerchantResponse> get(MerchantSearchRequest request) {
    Specification<Merchant> specification = (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getName())) {
        log.debug("searching for name: {}", request.getName());
        predicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("name")),
          "%" + request.getName().toLowerCase() + "%"
        ));
      }

      if (Objects.nonNull(request.getLocation())) {
        log.debug("searching for location: {}", request.getLocation());
        predicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("location")),
          "%" + request.getLocation().toLowerCase() + "%"
        ));
      }

      if (Objects.nonNull(request.getOpen())) {
        log.debug("searching for open: {}", request.getOpen());
        predicates.add(criteriaBuilder.equal(root.get("open"), request.getOpen()));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    };

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Merchant> merchants = merchantRepo.findAll(specification, pageable);
    List<MerchantResponse> responses = merchants.getContent()
      .stream()
      .map(this::toMerchantResponse)
      .toList();

    return new PageImpl<>(responses, pageable, merchants.getTotalElements());
  }

  @Transactional
  @Override
  public Page<MerchantResponse> getAllOpen(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Merchant> merchants = merchantRepo.findAllOpen(pageable);
    List<MerchantResponse> responses = merchants.getContent()
      .stream()
      .map(this::toMerchantResponse)
      .toList();

    return new PageImpl<>(responses, pageable, merchants.getTotalElements());
  }

  @Transactional
  @Override
  public MerchantResponse update(MerchantUpdateRequest request) {
    validationService.validate(request);

    checkId(request.getId());

    UUID id = UUID.fromString(request.getId());
    Merchant merchant = merchantRepo.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found"));

    if (merchantRepo.existsByNameAndLocation(request.getName(), request.getLocation())) {
      log.error("name and location already taken");
      throw new EntityExistsException("name and location already taken");
    }

    merchant.setName(request.getName());
    merchant.setLocation(request.getLocation());

    Merchant other = merchantRepo.save(merchant);

    if (!merchant.equals(other)) {
      log.error("failed to update merchant");
      throw new TransactionSystemException("failed to update merchant");
    }

    return toMerchantResponse(merchant);
  }

  @Transactional
  @Override
  public void updateStatus(String idString, boolean status) {
    checkId(idString);

    UUID id = UUID.fromString(idString);
    if (!merchantRepo.existsById(id)) {
      log.error("merchant not found");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found");
    }

    merchantRepo.updateStatus(id, status);
  }

  @Transactional
  @Override
  public void softDelete(String idString) {
    checkId(idString);

    UUID id = UUID.fromString(idString);
    if (!merchantRepo.existsById(id)) {
      log.error("merchant not found");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found");
    }

    merchantRepo.softDelete(id);
  }
}
