package com.marceljsh.binarfud.payload.response;

import com.marceljsh.binarfud.model.Merchant;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MerchantResponse {

  private UUID id;

  private String name;

  private String location;

  private boolean open;

  private boolean deleted;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static MerchantResponse of(Merchant merchant) {
    return MerchantResponse.builder()
        .id(merchant.getId())
        .name(merchant.getName())
        .location(merchant.getLocation())
        .open(merchant.isOpen())
        .deleted(merchant.isDeleted())
        .createdAt(merchant.getCreatedAt())
        .updatedAt(merchant.getUpdatedAt())
        .build();
  }
}
