package com.marceljsh.binarfud.user.service;

import com.marceljsh.binarfud.user.dto.UserChangePasswordRequest;
import com.marceljsh.binarfud.user.dto.UserResponse;
import com.marceljsh.binarfud.user.dto.UserSearchRequest;
import com.marceljsh.binarfud.user.dto.UserUpdateInfoRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {

  void deactivate(UUID id);

  void restore(UUID id);

  void remove(UUID id);

  UserResponse get(UUID id);

  UserResponse getByUsername(String username);

  UserResponse getByEmail(String email);

  Page<UserResponse> search(UserSearchRequest request);

  UserResponse updateInfo(UserUpdateInfoRequest request);

  void updatePassword(UserChangePasswordRequest request);

}
