package com.marceljsh.binarfud.model;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_merchants")

//@NamedNativeQuery(
//  name = "Merchant.updateInfo",
//  query = "SELECT * FROM update_merchant_info(:id, :name, :location)",
//  resultSetMapping = "MerchantResponseMapping"
//)
//
//@SqlResultSetMapping(
//  name = "MerchantResponseMapping",
//  classes = @ConstructorResult(
//    targetClass = MerchantResponse.class,
//    columns = {
//      @ColumnResult(name = "id", type = UUID.class),
//      @ColumnResult(name = "name", type = String.class),
//      @ColumnResult(name = "location", type = String.class),
//      @ColumnResult(name = "open", type = Boolean.class),
//      @ColumnResult(name = "created_at", type = LocalDateTime.class),
//      @ColumnResult(name = "updated_at", type = LocalDateTime.class)
//    }
//  )
//)
public class Merchant extends AuditableBase {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String location;

  public boolean open;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Merchant merchant = (Merchant) o;
    return Objects.equals(name, merchant.name) && Objects.equals(location, merchant.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, location);
  }
}
