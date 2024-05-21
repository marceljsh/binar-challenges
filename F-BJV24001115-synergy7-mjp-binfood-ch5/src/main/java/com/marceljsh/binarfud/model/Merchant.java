package com.marceljsh.binarfud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "tbl_merchants")
public class Merchant extends AuditableBase {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String location;

  public boolean open;


  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Merchant merchant = (Merchant) o;
    return Objects.equals(name, merchant.name) && Objects.equals(location, merchant.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, location);
  }
}
