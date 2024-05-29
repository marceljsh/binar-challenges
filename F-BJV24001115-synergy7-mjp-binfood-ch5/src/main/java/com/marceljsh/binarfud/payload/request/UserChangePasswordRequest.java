package com.marceljsh.binarfud.payload.request;

import com.marceljsh.binarfud.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangePasswordRequest {

  private String id;

  @NotBlank(message = "old password cannot be empty")
  @Pattern(regexp = Constants.Rgx.PASSWORD, message = "old password can only have letters, numbers, and special characters")
  @Size(min = 8, max = 64, message = "old password must be {min}-{max} characters")
  private String oldPassword;

  @NotBlank(message = "password cannot be empty")
  @Pattern(regexp = Constants.Rgx.PASSWORD, message = "password can only have letters, numbers, and special characters")
  @Size(min = 8, max = 64, message = "password must be {min}-{max} characters")
  private String password;

  @NotBlank(message = "confirm password cannot be empty")
  @Pattern(regexp = Constants.Rgx.PASSWORD, message = "confirm password can only have letters, numbers, and special characters")
  @Size(min = 8, max = 64, message = "confirm password must be {min}-{max} characters")
  private String confirmPassword;
}
