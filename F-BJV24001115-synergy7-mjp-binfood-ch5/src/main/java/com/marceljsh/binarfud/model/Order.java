package com.marceljsh.binarfud.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_orders")
public class Order extends AuditableBase {

  private boolean completed = Boolean.FALSE;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User owner;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(getCreatedAt(), order.getCreatedAt()) && Objects.equals(owner, order.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCreatedAt(), owner);
  }
}
