package com.marceljsh.binarfud.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchRequest {

  private String username;

  private String email;

  @NotNull(message = "Page number must be specified")
  private int page;

  @NotNull(message = "Page size must be specified")
  private int size;

}
