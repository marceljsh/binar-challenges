package com.marceljsh.binarfud.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  AuthResponse register(RegisterRequest request);

  AuthResponse authenticate(LoginRequest request);

  void logout(HttpServletRequest request);

}
