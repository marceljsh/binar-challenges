package com.marceljsh.binarfud.merchant;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MerchantResponse {

  private UUID id;

  private String name;

  private String location;

  private boolean open;

  public static MerchantResponse from(Merchant merchant) {
    return MerchantResponse.builder()
        .id(merchant.getId())
        .name(merchant.getName())
        .location(merchant.getLocation())
        .open(merchant.isOpen())
        .build();
  }

}
