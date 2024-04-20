package com.marceljsh.binfood.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  private UUID id;

  private String name;

  private Long price;

  private Merchant merchant;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Objects.equals(name, product.name) &&
      Objects.equals(merchant, product.merchant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, merchant);
  }
}
