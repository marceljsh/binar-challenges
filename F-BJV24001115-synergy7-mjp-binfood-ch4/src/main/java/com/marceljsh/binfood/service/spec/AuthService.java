package com.marceljsh.binfood.service.spec;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.UserLoginRequest;
import com.marceljsh.binfood.payload.response.TokenResponse;

public interface AuthService {

  TokenResponse login(UserLoginRequest request);
  void logout(User user);
}
