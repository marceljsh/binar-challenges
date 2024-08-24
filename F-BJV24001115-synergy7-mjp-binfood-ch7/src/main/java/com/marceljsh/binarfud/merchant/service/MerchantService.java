package com.marceljsh.binarfud.merchant.service;

import com.marceljsh.binarfud.merchant.dto.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.merchant.dto.MerchantAddRequest;
import com.marceljsh.binarfud.merchant.dto.MerchantResponse;
import com.marceljsh.binarfud.merchant.dto.MerchantSearchRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MerchantService {

  MerchantResponse save(MerchantAddRequest request);

  void deactivate(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  MerchantResponse get(UUID id);

  Page<MerchantResponse> search(MerchantSearchRequest request);

  void updateStatus(UUID id, boolean status);

  MerchantResponse updateInfo(MerchantUpdateInfoRequest request);

}
