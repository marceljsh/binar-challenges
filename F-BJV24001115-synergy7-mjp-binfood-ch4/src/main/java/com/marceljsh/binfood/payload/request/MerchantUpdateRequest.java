package com.marceljsh.binfood.payload.request;

import com.marceljsh.binfood.util.RegexConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantUpdateRequest {

  @NotBlank(message = "id cannot be empty")
  @Pattern(regexp = RegexConst.UUID, message = "id must be a valid UUID")
  @Size(min = 36, max = 36, message = "id must be {min} characters")
  private String id;

  @NotBlank(message = "name cannot be empty")
  @Pattern(regexp = RegexConst.DISPLAY_NAME, message = "name can only have letters, numbers, punctuation, and spaces")
  @Size(min = 3, max = 64, message = "name must be {min}-{max} characters")
  private String name;

  @NotBlank(message = "location cannot be empty")
  @Size(min = 3, max = 256, message = "location must be {min}-{max} characters")
  private String location;
}
