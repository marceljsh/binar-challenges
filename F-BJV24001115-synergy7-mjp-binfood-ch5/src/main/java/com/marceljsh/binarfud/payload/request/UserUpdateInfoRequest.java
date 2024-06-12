package com.marceljsh.binarfud.payload.request;

import com.marceljsh.binarfud.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateInfoRequest {

  private String id;

  @NotBlank(message = "username cannot be empty")
  @Pattern(regexp = Constants.Rgx.USERNAME, message = "name can only have letters, numbers, dots, and underscores")
  @Size(min = 3, max = 32, message = "username must be {min}-{max} characters")
  private String username;
}
