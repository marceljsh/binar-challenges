package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.exhandling.PageNotFoundException;
import com.marceljsh.binarfud.model.Merchant;
import com.marceljsh.binarfud.payload.request.MerchantAddRequest;
import com.marceljsh.binarfud.payload.request.MerchantSearchRequest;
import com.marceljsh.binarfud.payload.request.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.MerchantResponse;
import com.marceljsh.binarfud.repository.MerchantRepository;
import com.marceljsh.binarfud.service.spec.MerchantService;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
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
public class MerchantServiceImpl implements MerchantService {

  private final MerchantRepository merchantRepo;

  @Autowired
  public MerchantServiceImpl(MerchantRepository merchantRepo) {
    this.merchantRepo = merchantRepo;
  }

  @Transactional
  @Override
  public MerchantResponse save(MerchantAddRequest request) {
    if (merchantRepo.existsByNameAndLocation(request.getName(), request.getLocation())) {
      throw new EntityExistsException("merchant already exists");
    }

    Merchant merchant = Merchant.builder()
        .name(request.getName())
        .location(request.getLocation())
        .open(true)
        .build();

    Merchant other = merchantRepo.save(merchant);
    if (!merchant.equals(other)) {
      throw new TransactionSystemException("failed to save merchant");
    }

    return MerchantResponse.of(merchant);
  }

  @Transactional
  @Override
  public void deactivate(UUID id) {
    merchantRepo.softDelete(id);
  }

  @Transactional
  @Override
  public void restore(UUID id) {
    merchantRepo.restore(id);
  }

  @Transactional
  @Override
  public void remove(UUID id) {
    merchantRepo.deleteById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public MerchantResponse get(UUID id) {
    Merchant merchant = merchantRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Constants.Msg.MERCHANT_NOT_FOUND));

    return MerchantResponse.of(merchant);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<MerchantResponse> search(MerchantSearchRequest request) {
    Specification<Merchant> specification = ((root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (Objects.nonNull(request.getName())) {
        predicates.add(criteriaBuilder.like(
            criteriaBuilder.lower(root.get("name")),
            "%" + request.getName().toLowerCase() + "%"));
      }

      if (Objects.nonNull(request.getLocation())) {
        predicates.add(criteriaBuilder.like(
            criteriaBuilder.lower(root.get("location")),
            "%" + request.getLocation().toLowerCase() + "%"));
      }

      if (Objects.nonNull(request.getOpen())) {
        predicates.add(criteriaBuilder.equal(root.get("open"), request.getOpen()));
      }

      return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
    });

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Merchant> merchants = merchantRepo.findAll(specification, pageable);

    if (merchants.getTotalElements() == 0) {
      return new PageImpl<>(List.of(), pageable, 0);
    }

    if (request.getPage() > merchants.getTotalPages() - 1) {
      throw new PageNotFoundException(
          String.format("page %d out of bounds (%s)", request.getPage() + 1, merchants.getTotalPages()));
    }

    List<MerchantResponse> result = merchants.getContent()
        .stream()
        .map(MerchantResponse::of)
        .toList();

    return new PageImpl<>(result, pageable, merchants.getTotalElements());
  }

  @Transactional
  @Override
  public void updateStatus(UUID id, boolean status) {
    if (!merchantRepo.existsById(id)) {
      throw new EntityNotFoundException(Constants.Msg.MERCHANT_NOT_FOUND);
    }

    merchantRepo.updateStatus(id, status);
  }

  @Transactional
  @Override
  public MerchantResponse updateInfo(MerchantUpdateInfoRequest request) {
    UUID id = UUID.fromString(request.getId());
    if (!merchantRepo.existsById(id)) {
      throw new EntityNotFoundException(Constants.Msg.MERCHANT_NOT_FOUND);
    }

    Merchant merchant = merchantRepo.updateInfo(id, request.getName(), request.getLocation());

    return MerchantResponse.of(merchant);
  }
}
