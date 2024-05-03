package com.marceljsh.binfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SQLRestriction("deleted_at IS NULL")
@Table(name = "tbl_orders")
public class Order extends AuditableBase {

  private boolean completed;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(getCreatedAt(), order.getCreatedAt()) && Objects.equals(user, order.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCreatedAt(), user);
  }
}
