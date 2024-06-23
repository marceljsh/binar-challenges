package com.marceljsh.binarfud.merchant;

import com.marceljsh.binarfud.common.AuditableBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "tbl_merchants")
public class Merchant extends AuditableBase {

  @Column(nullable = false, length = 80)
  private String name;

  @Column(nullable = false)
  private String location;

  private boolean open;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Merchant merchant = (Merchant) o;
    return Objects.equals(name, merchant.name) &&
        Objects.equals(location, merchant.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, location);
  }

}
