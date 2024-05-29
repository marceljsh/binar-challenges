package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.MerchantAddRequest;
import com.marceljsh.binarfud.payload.request.MerchantSearchRequest;
import com.marceljsh.binarfud.payload.request.MerchantUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.MerchantResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MerchantService {

  MerchantResponse save(MerchantAddRequest request);

  void deactivate(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  MerchantResponse get(UUID id);

  Page<MerchantResponse> search(MerchantSearchRequest request);

  void updateStatus(UUID id, boolean open);

  MerchantResponse updateInfo(MerchantUpdateInfoRequest request);
}
