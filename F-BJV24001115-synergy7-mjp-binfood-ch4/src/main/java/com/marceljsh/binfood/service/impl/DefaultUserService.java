package com.marceljsh.binfood.service.impl;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.UserRegisterRequest;
import com.marceljsh.binfood.payload.request.UserUpdateInfoRequest;
import com.marceljsh.binfood.payload.request.UserUpdatePasswordRequest;
import com.marceljsh.binfood.payload.response.UserResponse;
import com.marceljsh.binfood.repository.UserRepo;
import com.marceljsh.binfood.security.BCrypt;
import com.marceljsh.binfood.service.spec.UserService;
import com.marceljsh.binfood.service.spec.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class DefaultUserService implements UserService {

  private final UserRepo userRepo;

  private final ValidationService validationService;

  @Autowired
  public DefaultUserService(UserRepo userRepo, ValidationService validationService) {
    this.userRepo = userRepo;
    this.validationService = validationService;
  }

  private UserResponse toUserResponse(User user) {
    return UserResponse.builder()
      .id(user.getId())
      .username(user.getUsername())
      .email(user.getEmail())
      .build();
  }

  @Transactional
  @Override
  public UserResponse register(UserRegisterRequest request) {
    validationService.validate(request);

    if (!request.getPassword().equals(request.getConfirmPassword())) {
      throw new DataIntegrityViolationException("password and confirm password do not match");
    }

    if (userRepo.existsByEmail(request.getEmail())) {
      throw new DataIntegrityViolationException("email is taken");
    }

    if (userRepo.existsByUsername(request.getEmail())) {
      throw new DataIntegrityViolationException("username is taken");
    }

    User user = User.builder()
      .username(request.getUsername())
      .email(request.getEmail())
      .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
      .build();

    User other = userRepo.save(user);
    if (!user.equals(other)) {
      throw new TransactionSystemException("failed to register");
    }

    return toUserResponse(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse findById(String id) {
    if (!validationService.isValidUUID(id)) {
      throw new IllegalArgumentException("invalid id");
    }

    UUID userId = UUID.fromString(id);
    User user = userRepo.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("user not found"));

    return toUserResponse(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse get(User user) {
    return toUserResponse(user);
  }

  @Transactional
  @Override
  public UserResponse update(User user, UserUpdateInfoRequest request) {
    validationService.validate(request);

    if (userRepo.existsByUsername(request.getUsername())) {
      throw new DataIntegrityViolationException("username is taken");
    }

    user.setUsername(request.getUsername());

    User other = userRepo.save(user);
    if (!user.equals(other)) {
      throw new TransactionSystemException("failed to update");
    }

    return toUserResponse(user);
  }

  @Transactional
  @Override
  public String changePassword(User user, UserUpdatePasswordRequest request) {
    validationService.validate(request);

    if (!BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
      throw new IllegalArgumentException("incorrect password");
    }

    if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
      throw new IllegalArgumentException("new password and confirm new password do not match");
    }

    user.setPassword(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));

    User other = userRepo.save(user);
    if (!user.equals(other)) {
      throw new TransactionSystemException("failed to change password");
    }

    return "OK";
  }

  @Transactional
  @Override
  public void softDelete(User user) {
    userRepo.softDelete(user.getId());
  }
}
