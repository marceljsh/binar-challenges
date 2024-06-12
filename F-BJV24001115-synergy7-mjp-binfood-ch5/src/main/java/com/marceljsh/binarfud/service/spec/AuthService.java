package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.UserLoginRequest;
import com.marceljsh.binarfud.payload.request.UserRegisterRequest;
import com.marceljsh.binarfud.payload.response.AuthResponse;

public interface AuthService {

  AuthResponse register(UserRegisterRequest request);

  AuthResponse authenticate(UserLoginRequest request);

  void signOut(String token);
}
