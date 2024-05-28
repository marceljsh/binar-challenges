package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.model.User;
import com.marceljsh.binarfud.payload.request.UserRegisterRequest;
import com.marceljsh.binarfud.payload.response.UserResponse;
import com.marceljsh.binarfud.repository.UserRepository;
import com.marceljsh.binarfud.service.spec.UserService;
import com.marceljsh.binarfud.util.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class UserServiceImpl implements UserService {

  private final UserRepository userRepo;

  @Autowired
  public UserServiceImpl(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Transactional
  @Override
  public void softDelete(UUID id) {
    userRepo.softDelete(id);
  }

  @Transactional
  @Override
  public UserResponse register(UserRegisterRequest request) {
    if (userRepo.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("email is already registered");
    }

    if (userRepo.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("username is already taken");
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

    return UserResponse.of(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse get(User user) {
    return UserResponse.of(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse findById(UUID id) {
    User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));

    return UserResponse.of(user);
  }
}
