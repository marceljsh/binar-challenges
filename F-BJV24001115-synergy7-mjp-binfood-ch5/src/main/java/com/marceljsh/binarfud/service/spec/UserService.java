package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.model.User;
import com.marceljsh.binarfud.payload.request.UserRegisterRequest;
import com.marceljsh.binarfud.payload.response.UserResponse;

import java.util.UUID;

public interface UserService {

  void softDelete(UUID id);

  UserResponse register(UserRegisterRequest request);

  UserResponse get(User user);

  UserResponse findById(UUID id);
}
