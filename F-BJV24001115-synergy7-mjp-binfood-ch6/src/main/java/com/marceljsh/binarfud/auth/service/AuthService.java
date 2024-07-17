package com.marceljsh.binarfud.auth.service;

import com.marceljsh.binarfud.auth.dto.AuthResponse;
import com.marceljsh.binarfud.auth.dto.LoginRequest;
import com.marceljsh.binarfud.auth.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

  void addGod();

  void registerOAuth2(String email);

  AuthResponse register(RegisterRequest request);

  AuthResponse authenticate(LoginRequest request);

  void logout(HttpServletRequest request);
}
