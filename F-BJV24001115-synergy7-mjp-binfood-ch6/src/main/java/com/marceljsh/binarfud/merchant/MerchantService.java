package com.marceljsh.binarfud.merchant;

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
