package com.marceljsh.binarfud.payload.response;

import com.marceljsh.binarfud.model.User;
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

  public static UserResponse of(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .active(!user.isDeleted())
        .build();
  }
}
