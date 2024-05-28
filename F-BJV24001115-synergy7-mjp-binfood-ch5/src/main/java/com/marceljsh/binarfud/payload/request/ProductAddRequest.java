package com.marceljsh.binarfud.payload.request;

import com.marceljsh.binarfud.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
public class ProductAddRequest {

  @NotBlank(message = "name cannot be empty")
  @Pattern(regexp = Constants.Rgx.DISPLAY_NAME, message = "name can only have alphanumeric characters and spaces")
  @Size(min = 3, max = 64, message = "name must be {min}-{max} characters")
  private String name;

  @NotBlank(message = "price cannot be empty")
  @PositiveOrZero(message = "price must be zero or greater")
  private long price;

  @NotBlank(message = "seller_id cannot be empty")
  @UUID(message = "seller_id must be a valid UUID")
  private String sellerId;
}
