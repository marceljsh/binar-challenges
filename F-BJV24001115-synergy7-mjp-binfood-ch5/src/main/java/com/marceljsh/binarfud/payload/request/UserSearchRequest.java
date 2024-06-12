package com.marceljsh.binarfud.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchRequest {

  private String username;

  private String email;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
