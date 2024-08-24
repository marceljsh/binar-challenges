package com.marceljsh.binarfud.order.model;

import com.marceljsh.binarfud.common.model.Auditable;
import com.marceljsh.binarfud.orderdetail.model.OrderDetail;
import com.marceljsh.binarfud.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_orders")
public class Order extends Auditable {

  @Column(nullable = false)
  private LocalDateTime orderTime;

  @Column(nullable = false)
  private String destination;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "user_id",
    referencedColumnName = "id",
    nullable = false
  )
  private User owner;

  @Column(nullable = false)
  private boolean completed;

  @OneToMany(mappedBy = "order")
  private List<OrderDetail> orderDetails;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Order order = (Order) o;
    return Objects.equals(getCreatedAt(), order.getCreatedAt()) &&
        Objects.equals(owner, order.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCreatedAt(), owner);
  }

}
