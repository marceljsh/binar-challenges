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
public class UserUpdateInfoRequest {

  // temporary constraints for username due to
  // it being the only field in this request
  @NotBlank(message = "username cannot be empty")
  @Pattern(regexp = RegexConst.USERNAME, message = "name can only have letters, numbers, dots, and underscores")
  @Size(min = 3, max = 32, message = "username must be {min}-{max} characters")
  private String username;

  // other fields e.g. address, phone, etc.
}
