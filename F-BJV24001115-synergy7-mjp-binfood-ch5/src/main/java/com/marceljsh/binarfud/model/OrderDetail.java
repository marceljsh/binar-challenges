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

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_order_details")
public class OrderDetail extends AuditableBase {

  @ManyToOne
  @JoinColumn(
    name = "order_id",
    referencedColumnName = "id",
    nullable = false
  )
  private Order order;

  @ManyToOne
  @JoinColumn(
    name = "product_id",
    referencedColumnName = "id",
    nullable = false
  )
  private Product product;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private BigDecimal totalPrice;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    OrderDetail that = (OrderDetail) o;
    return order.equals(that.order) && product.equals(that.product);
  }

  @Override
  public int hashCode() {
    return Objects.hash(order, product);
  }
}
