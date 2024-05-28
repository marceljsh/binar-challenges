package com.marceljsh.binarfud.payload.request;

import com.marceljsh.binarfud.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterRequest {

  @NotBlank(message = "username cannot be empty")
  @Pattern(regexp = Constants.Rgx.USERNAME, message = "name can only have letters, numbers, dots, and underscores")
  @Size(min = 3, max = 32, message = "username must be {min}-{max} characters")
  private String username;

  @NotBlank(message = "email cannot be empty")
  @Email
  @Size(max = 50)
  private String email;

  @NotBlank(message = "password cannot be empty")
  @Pattern(regexp = Constants.Rgx.PASSWORD, message = "password can only have letters, numbers, and special characters")
  @Size(min = 8, max = 64, message = "password must be {min}-{max} characters")
  private String password;

  @NotBlank(message = "confirm password cannot be empty")
  @Pattern(regexp = Constants.Rgx.PASSWORD, message = "password can only have letters, numbers, and special characters")
  @Size(min = 8, max = 64, message = "password must be {min}-{max} characters")
  private String confirmPassword;
}
