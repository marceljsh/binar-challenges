package com.marceljsh.binarfud.product.model;

import com.marceljsh.binarfud.common.model.AuditableBase;
import com.marceljsh.binarfud.merchant.model.Merchant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_products")
public class Product extends AuditableBase {

  @Column(length = 80, nullable = false)
  private String name;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private int stock;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "seller_id",
    nullable = false,
    referencedColumnName = "id"
  )
  private Merchant seller;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Product that = (Product) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(seller, that.seller);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, seller);
  }

}
