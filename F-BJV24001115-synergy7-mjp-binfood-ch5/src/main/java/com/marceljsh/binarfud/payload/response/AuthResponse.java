package com.marceljsh.binarfud.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

  private String token;
}
