package com.marceljsh.binarfud.user;

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
