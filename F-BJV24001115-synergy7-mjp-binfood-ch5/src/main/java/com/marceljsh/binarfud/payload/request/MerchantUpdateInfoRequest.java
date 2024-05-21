package com.marceljsh.binarfud.payload.request;

import com.marceljsh.binarfud.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
public class MerchantUpdateInfoRequest {

  @UUID
  @NotBlank(message = "id cannot be empty")
  @Size(min = 36, max = 36, message = "id must be {min} characters")
  private String id;

  @NotBlank(message = "name cannot be empty")
  @Pattern(regexp = Constants.REGEX_DISPLAY_NAME, message = "name can only have letters, numbers, punctuation, and spaces")
  @Size(min = 3, max = 64, message = "name must be {min}-{max} characters")
  private String name;

  @NotBlank(message = "location cannot be empty")
  @Size(min = 3, max = 256, message = "location must be {min}-{max} characters")
  private String location;
}
