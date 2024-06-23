package com.marceljsh.binarfud.merchant;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantSearchRequest {

  private String name;

  private String location;

  private Boolean open;

  @NotNull(message = "Page number must be specified")
  private int page;

  @NotNull(message = "Page size must be specified")
  private int size;

}
