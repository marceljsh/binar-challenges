package com.marceljsh.binarfud.payload.response;

import com.marceljsh.binarfud.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEagerResponse extends ProductResponse {

  private MerchantResponse seller;

  public static ProductEagerResponse of(Product product) {
    return ProductEagerResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .deleted(product.isDeleted())
        .seller(MerchantResponse.of(product.getSeller()))
        .build();
  }
}
