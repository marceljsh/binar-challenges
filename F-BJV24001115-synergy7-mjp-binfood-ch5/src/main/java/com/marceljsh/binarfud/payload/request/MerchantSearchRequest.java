package com.marceljsh.binarfud.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantSearchRequest {

  private String name;

  private String location;

  private Boolean open;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
