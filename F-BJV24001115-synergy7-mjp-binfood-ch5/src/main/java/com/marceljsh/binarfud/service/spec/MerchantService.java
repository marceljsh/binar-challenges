package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.MerchantAddRequest;
import com.marceljsh.binarfud.payload.request.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.MerchantResponse;

import java.util.UUID;

public interface MerchantService {

  void softDelete(UUID id);
  void remove(UUID id);
  MerchantResponse save(MerchantAddRequest request);

  MerchantResponse findById(UUID id);

  void updateStatus(UUID id, boolean open);
  MerchantResponse updateInfo(MerchantUpdateInfoRequest request);
}
