package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.MerchantAddRequest;
import com.marceljsh.binarfud.payload.request.MerchantSearchRequest;
import com.marceljsh.binarfud.payload.request.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.MerchantResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MerchantService {

  MerchantResponse save(MerchantAddRequest request);

  void softDelete(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  MerchantResponse findById(UUID id);

  Page<MerchantResponse> get(MerchantSearchRequest request);

  void updateStatus(UUID id, boolean open);

  MerchantResponse updateInfo(MerchantUpdateInfoRequest request);
}
