package com.marceljsh.binarfud.auth.dto;

import com.marceljsh.binarfud.app.util.Regexp;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

  @Size(
    min = 3,
    max = 32,
    message = "Username must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.USERNAME,
    message = "Username can only have letters, numbers, dots, and underscores"
  )
  private String username;

  @Size(
    min = 8,
    max = 60,
    message = "Password must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.RAW_PASSWORD,
    message = "Password can only have letters, numbers, and special characters"
  )
  private String password;

}
