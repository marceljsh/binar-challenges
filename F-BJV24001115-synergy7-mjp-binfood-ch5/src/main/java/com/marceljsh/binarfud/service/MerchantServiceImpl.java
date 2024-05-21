package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.exhandling.ResourceNotFoundException;
import com.marceljsh.binarfud.model.Merchant;
import com.marceljsh.binarfud.payload.request.MerchantAddRequest;
import com.marceljsh.binarfud.payload.request.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.MerchantResponse;
import com.marceljsh.binarfud.repository.MerchantRepository;
import com.marceljsh.binarfud.service.spec.MerchantService;
import com.marceljsh.binarfud.util.Constants;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class MerchantServiceImpl implements MerchantService {

  private final MerchantRepository merchantRepo;

  @Autowired
  public MerchantServiceImpl(MerchantRepository merchantRepo) {
    this.merchantRepo = merchantRepo;
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

  @Transactional
  @Override
  public void softDelete(UUID id) {
    merchantRepo.softDelete(id);
  }

  @Transactional
  @Override
  public void remove(UUID id) {
    merchantRepo.deleteById(id);
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
        .open(false)
        .build();

    Merchant other = merchantRepo.save(merchant);

    if (!merchant.equals(other)) {
      throw new TransactionSystemException("failed to save merchant");
    }

    return toMerchantResponse(merchant);
  }

  @Transactional(readOnly = true)
  @Override
  public MerchantResponse findById(UUID id) {
    Merchant merchant = merchantRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(Constants.MSG_MERCHANT_NOT_FOUND));

    return toMerchantResponse(merchant);
  }

  @Transactional
  @Override
  public void updateStatus(UUID id, boolean status) {
    if (!merchantRepo.existsById(id)) {
      throw new ResourceNotFoundException(Constants.MSG_MERCHANT_NOT_FOUND);
    }

    merchantRepo.updateStatus(id, status);
  }

  @Transactional
  @Override
  public MerchantResponse updateInfo(MerchantUpdateInfoRequest request) {
    UUID id = UUID.fromString(request.getId());
    
    return merchantRepo.updateInfo(id, request.getName(), request.getLocation());
  }
}
