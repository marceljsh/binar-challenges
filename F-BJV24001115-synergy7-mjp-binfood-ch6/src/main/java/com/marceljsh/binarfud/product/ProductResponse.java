package com.marceljsh.binarfud.product;

import com.marceljsh.binarfud.merchant.MerchantResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {

  private UUID id;

  private String name;

  private BigDecimal price;

  private Integer stock;

  private MerchantResponse merchant;

  public static ProductResponse from(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .stock(product.getStock())
        .merchant(MerchantResponse.from(product.getSeller()))
        .build();
  }

}
