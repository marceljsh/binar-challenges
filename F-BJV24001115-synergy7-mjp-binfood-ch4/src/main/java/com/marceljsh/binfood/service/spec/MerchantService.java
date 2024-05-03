package com.marceljsh.binfood.service.spec;

import com.marceljsh.binfood.payload.request.MerchantAddRequest;
import com.marceljsh.binfood.payload.request.MerchantSearchRequest;
import com.marceljsh.binfood.payload.request.MerchantUpdateRequest;
import com.marceljsh.binfood.payload.response.MerchantResponse;
import org.springframework.data.domain.Page;

public interface MerchantService {

  MerchantResponse save(MerchantAddRequest request);
  void remove(String idString);
  MerchantResponse findById(String idString);
  Page<MerchantResponse> get(MerchantSearchRequest request);
  MerchantResponse update(MerchantUpdateRequest request);
  void updateStatus(String idString, boolean status);
  Page<MerchantResponse> getAllOpen(int page, int size);
  void softDelete(String idString);
}
