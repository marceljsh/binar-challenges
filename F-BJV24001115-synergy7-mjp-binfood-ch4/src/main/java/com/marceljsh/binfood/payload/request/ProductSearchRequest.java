package com.marceljsh.binfood.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSearchRequest {

  private String name;

  @NotNull
  private Long minPrice;

  @NotNull
  private Long maxPrice;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
