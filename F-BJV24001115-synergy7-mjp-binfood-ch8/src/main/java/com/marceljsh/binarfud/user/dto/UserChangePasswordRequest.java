package com.marceljsh.binarfud.user.dto;

import com.marceljsh.binarfud.app.util.Regexp;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserChangePasswordRequest {

  private UUID id;

  @Size(
    min = 8,
    max = 64,
    message = "Old password must be {min}-{max} characters"
  )
  @Pattern(
    regexp = Regexp.RAW_PASSWORD,
    message = "Old password can only have letters, numbers, and special characters"
  )
  private String oldPassword;

  @Size(
    min = 8,
    max = 64,
    message = "New password must be {min}-{max} characters"
  )
  @Pattern(
    regexp = Regexp.RAW_PASSWORD,
    message = "New password can only have letters, numbers, and special characters"
  )
  private String newPassword;

  @Size(
    min = 8,
    max = 64,
    message = "Confirm password must be {min}-{max} characters"
  )
  @Pattern(
    regexp = Regexp.RAW_PASSWORD,
    message = "Confirm password can only have letters, numbers, and special characters"
  )
  private String confirmPassword;

}
