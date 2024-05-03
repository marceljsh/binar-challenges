package com.marceljsh.binfood.service.spec;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.UserRegisterRequest;
import com.marceljsh.binfood.payload.request.UserUpdateInfoRequest;
import com.marceljsh.binfood.payload.request.UserUpdatePasswordRequest;
import com.marceljsh.binfood.payload.response.UserResponse;

public interface UserService {

  UserResponse register(UserRegisterRequest request);
  UserResponse findById(String id);
  UserResponse get(User user);
  UserResponse update(User user, UserUpdateInfoRequest request);
  String changePassword(User user, UserUpdatePasswordRequest request);
  void softDelete(User user);
}
