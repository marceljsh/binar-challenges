package com.marceljsh.binfood.payload.request;

import com.marceljsh.binfood.util.RegexConst;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpdateRequest {

  @NotBlank(message = "id cannot be empty")
  @Pattern(regexp = RegexConst.UUID, message = "id must be a valid UUID")
  @Size(min = 36, max = 36, message = "id must be {min} characters")
  private String id;

  @NotBlank(message = "name cannot be empty")
  @Pattern(regexp = RegexConst.DISPLAY_NAME, message = "name can only have letters, numbers, punctuation, and spaces")
  @Size(min = 3, max = 64, message = "name must be {min}-{max} characters")
  private String name;

  @Min(value = 1, message = "price must be greater than or equal to {value}")
  @Max(value = Long.MAX_VALUE, message = "price must be less than or equal to {value}")
  private long price;
}
