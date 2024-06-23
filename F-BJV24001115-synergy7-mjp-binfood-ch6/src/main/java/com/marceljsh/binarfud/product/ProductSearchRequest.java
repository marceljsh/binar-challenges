package com.marceljsh.binarfud.product;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductSearchRequest {

  private String name;

  private BigDecimal minPrice;

  private BigDecimal maxPrice;

  private String seller;

  @NotNull(message = "Page number must be specified")
  private int page;

  @NotNull(message = "Page size must be specified")
  private int size;

}
