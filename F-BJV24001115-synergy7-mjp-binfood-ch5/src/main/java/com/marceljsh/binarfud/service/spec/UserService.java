package com.marceljsh.binarfud.service.spec;

import com.marceljsh.binarfud.payload.request.UserChangePasswordRequest;
import com.marceljsh.binarfud.payload.request.UserRegisterRequest;
import com.marceljsh.binarfud.payload.request.UserSearchRequest;
import com.marceljsh.binarfud.payload.request.UserUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {

  UserResponse register(UserRegisterRequest request);

  void deactivate(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  UserResponse get(UUID id);

  // UserResponse findByUsername(String username);

  // UserResponse findByEmail(String email);

  Page<UserResponse> search(UserSearchRequest request);

  UserResponse updateInfo(UserUpdateInfoRequest request);

  void updatePassword(UserChangePasswordRequest request);
}
