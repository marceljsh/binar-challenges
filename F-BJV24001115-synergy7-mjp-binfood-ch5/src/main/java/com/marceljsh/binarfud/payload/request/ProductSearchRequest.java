package com.marceljsh.binarfud.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductSearchRequest {

  private String name;

  private UUID sellerId;

  private BigDecimal minPrice;

  private BigDecimal maxPrice;

  private boolean eager;

  @NotNull
  private Integer page;
  @NotNull
  private Integer size;
}
