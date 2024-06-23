package com.marceljsh.binarfud.user;

import com.marceljsh.binarfud.common.Regexp;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserUpdateInfoRequest {

  private UUID id;

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

}
