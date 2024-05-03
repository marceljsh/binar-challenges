package com.marceljsh.binfood.payload.request;

import com.marceljsh.binfood.util.RegexConst;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAddRequest {

  @NotBlank(message = "name cannot be empty")
  @Pattern(regexp = RegexConst.DISPLAY_NAME, message = "name can only have letters, numbers, punctuation, and spaces")
  @Size(min = 3, max = 64, message = "name must be {min}-{max} characters")
  private String name;

  @Min(value = 1, message = "price must be greater than or equal to {value}")
  @Max(value = Long.MAX_VALUE, message = "price must be less than or equal to {value}")
  private long price;

  @NotBlank(message = "seller_id cannot be empty")
  @Pattern(regexp = RegexConst.UUID, message = "invalid seller")
  @Size(min = 36, max = 36, message = "seller_id must be {min} characters")
  private String sellerId;
}
