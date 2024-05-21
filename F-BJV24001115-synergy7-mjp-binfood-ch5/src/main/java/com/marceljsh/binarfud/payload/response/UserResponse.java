package com.marceljsh.binarfud.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

  private UUID id;

  private String username;

  private String email;
}
