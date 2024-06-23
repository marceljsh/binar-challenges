package com.marceljsh.binarfud.user;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

  private UUID id;

  private String username;

  private String email;

  private boolean active;

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .active(user.isActive())
        .build();
  }

}
