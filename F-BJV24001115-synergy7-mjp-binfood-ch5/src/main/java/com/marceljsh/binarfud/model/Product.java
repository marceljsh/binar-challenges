package com.marceljsh.binarfud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_products")
public class Product extends AuditableBase {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private long price;

  @ManyToOne
  @JoinColumn(name = "seller_id", referencedColumnName = "id")
  private Merchant seller;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Product product = (Product) o;
    return price == product.price &&
        Objects.equals(name, product.name) &&
        Objects.equals(seller, product.getSeller());
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, price, seller);
  }
}
