package com.marceljsh.binfood.payload.request;

import com.marceljsh.binfood.util.RegexConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordRequest {

  @NotBlank(message = "old password cannot be empty")
  @Pattern(regexp = RegexConst.PASSWORD, message = "password can only have letters, numbers, and special characters")
  @Size(min = 8, max = 64, message = "password must be {min}-{max} characters")
  private String oldPassword;

  @NotBlank(message = "old password cannot be empty")
  @Pattern(regexp = RegexConst.PASSWORD, message = "password can only have letters, numbers, and special characters")
  @Size(min = 8, max = 64, message = "password must be {min}-{max} characters")
  private String newPassword;

  @NotBlank(message = "old password cannot be empty")
  @Pattern(regexp = RegexConst.PASSWORD, message = "password can only have letters, numbers, and special characters")
  @Size(min = 8, max = 64, message = "password must be {min}-{max} characters")
  private String confirmNewPassword;
}
