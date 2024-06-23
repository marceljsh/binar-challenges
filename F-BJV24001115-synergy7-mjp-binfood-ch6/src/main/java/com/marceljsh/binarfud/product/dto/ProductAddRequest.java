package com.marceljsh.binarfud.product.dto;

import com.marceljsh.binarfud.app.util.Regexp;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductAddRequest {

  @Size(
    min = 3,
    max = 80,
    message = "Product name must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.DISPLAY_NAME,
    message = "Product name can only have alphanumeric characters and spaces"
  )
  private String name;

  @PositiveOrZero(message = "Product price must be a positive number")
  private BigDecimal price;

  @PositiveOrZero(message = "Product stock must be a positive number")
  private Integer stock;

  @org.hibernate.validator.constraints.UUID(
    message = "Seller ID must be a valid UUID"
  )
  private UUID sellerId;

}
