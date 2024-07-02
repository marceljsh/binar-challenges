package com.marceljsh.binarfud.merchant.service;

import com.marceljsh.binarfud.app.util.Constants;
import com.marceljsh.binarfud.app.exception.PageNotFoundException;
import com.marceljsh.binarfud.merchant.dto.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.merchant.dto.MerchantAddRequest;
import com.marceljsh.binarfud.merchant.dto.MerchantResponse;
import com.marceljsh.binarfud.merchant.dto.MerchantSearchRequest;
import com.marceljsh.binarfud.merchant.model.Merchant;
import com.marceljsh.binarfud.merchant.repository.MerchantRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

  private final Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);

  private final MerchantRepository merchantRepo;

  @Override
  @Transactional
  public MerchantResponse save(MerchantAddRequest request) {
    log.trace("Creating new merchant: name={}, location={}", request.getName(), request.getLocation());

    if (merchantRepo.existsByNameAndLocation(request.getName(), request.getLocation())) {
      log.error("Merchant {}@{} already exists", request.getName(), request.getLocation());
      throw new EntityExistsException("Merchant already exists");
    }

    Merchant merchant = Merchant.builder()
        .name(request.getName())
        .location(request.getLocation())
        .open(true)
        .build();

    log.info("Saving new merchant: {}", merchant);
    return MerchantResponse.from(merchantRepo.save(merchant));
  }

  @Override
  @Transactional
  public void deactivate(UUID id) {
    log.trace("Deactivating merchant with id: {}", id);

    if (merchantRepo.existsById(id)) {
      log.info("Proceeding to deactivate found merchant with id: {}", id);
      merchantRepo.deactivateById(id);
    }
  }

  @Override
  @Transactional
  public void restore(UUID id) {
    log.trace("Restoring merchant with id: {}", id);

    if (merchantRepo.existsById(id)) {
      log.info("Proceeding to restore found merchant with id: {}", id);
      merchantRepo.restoreById(id);
    }
  }

  @Override
  @Transactional
  public void remove(UUID id) {
    log.trace("Removing merchant with id: {}", id);

    if (merchantRepo.existsById(id)) {
      log.info("Proceeding to remove merchant with id: {}", id);
      merchantRepo.deleteById(id);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public MerchantResponse get(UUID id) {
    log.trace("Fetching merchant with id: {}", id);

    Merchant merchant = merchantRepo.findById(id).orElse(null);
    if (Objects.isNull(merchant)) {
      log.error("Merchant with id: {} not found", id);
      throw new EntityNotFoundException(Constants.MSG_MERCHANT_NOT_FOUND);
    }

    log.info("Found merchant {} in {}",
        merchant.getName(), merchant.getLocation());

    return MerchantResponse.from(merchant);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MerchantResponse> search(MerchantSearchRequest request) {
    log.trace(
        "Searching for merchants: name={}, location={}, status={}, page={}, size={}",
        request.getName(),
        request.getLocation(),
        request.getOpen(),
        request.getPage(),
        request.getSize());

    Specification<Merchant> specification = ((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getName())) {
        predicates.add(cb.like(
            cb.lower(root.get("name")),
            "%" + request.getName().toLowerCase() + "%"));
      }

      if (Objects.nonNull(request.getLocation())) {
        predicates.add(cb.like(
            cb.lower(root.get("location")),
            "%" + request.getLocation().toLowerCase() + "%"));
      }

      if (Objects.nonNull(request.getOpen())) {
        predicates.add(
            cb.equal(root.get("open"),
                request.getOpen()));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    });

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Merchant> merchants = merchantRepo.findAll(specification, pageable);

    if (merchants.getTotalElements() == 0) {
      log.info("Merchant search returned no results");
      return new PageImpl<>(List.of(), pageable, 0);
    }

    if (request.getPage() + 1 > merchants.getTotalPages()) {
      String error = String.format(
          "page %d out of bounds (%d)",
          request.getPage() + 1,
          merchants.getTotalPages());

      log.error(error);
      throw new PageNotFoundException(error);
    }

    log.info("Found {} merchants", merchants.getTotalElements());

    List<MerchantResponse> result = merchants.getContent()
        .stream()
        .map(MerchantResponse::from)
        .toList();

    return new PageImpl<>(result, pageable, merchants.getTotalElements());
  }

  @Override
  @Transactional
  public void updateStatus(UUID id, boolean status) {
    log.trace("Updating merchant status: id={}, status={}", id, status);

    if (merchantRepo.existsById(id)) {
      log.info("Proceeding to update status of found merchant with id: {}", id);
      merchantRepo.updateOpenStatus(id, status);
    }
  }

  @Override
  @Transactional
  public MerchantResponse updateInfo(MerchantUpdateInfoRequest request) {
    log.trace("Updating info for merchant: {}", request.getId());

    Merchant merchant = merchantRepo.findById(request.getId()).orElse(null);
    if (Objects.isNull(merchant)) {
      log.error("Merchant with id: {} not found", request.getId());
      throw new EntityNotFoundException(Constants.MSG_MERCHANT_NOT_FOUND);
    }

    merchant.setName(request.getName());
    merchant.setLocation(request.getLocation());

    log.info("Saving updated merchant: {}", merchant.getName());

    return MerchantResponse.from(merchantRepo.save(merchant));
  }

}
