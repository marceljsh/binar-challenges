package com.marceljsh.binarfud.payload.response;

import com.marceljsh.binarfud.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

  protected UUID id;

  protected String name;

  protected long price;

  protected boolean deleted;

  public static ProductResponse of(UUID id, String name, long price, boolean deleted) {
    return new ProductResponse(id, name, price, deleted);
  }

  public static ProductResponse of(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .deleted(product.isDeleted())
        .build();
  }
}
