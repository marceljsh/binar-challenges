package com.marceljsh.binarfud.auth.dto;

import com.marceljsh.binarfud.user.model.User;
import com.marceljsh.binarfud.user.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

  private UserResponse user;

  private String token;

  public static AuthResponse of(User user, String token) {
    return AuthResponse.builder()
        .user(UserResponse.from(user))
        .token(token)
        .build();
  }

}
