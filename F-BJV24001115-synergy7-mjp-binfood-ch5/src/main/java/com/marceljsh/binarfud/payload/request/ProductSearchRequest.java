package com.marceljsh.binarfud.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductSearchRequest {

  private String name;

  private UUID sellerId;

  private Long minPrice;

  private Long maxPrice;

  private boolean eager;

  @NotNull
  private Integer page;
  @NotNull
  private Integer size;
}
