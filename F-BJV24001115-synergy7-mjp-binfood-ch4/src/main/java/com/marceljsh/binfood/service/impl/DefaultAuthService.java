package com.marceljsh.binfood.service.impl;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.UserLoginRequest;
import com.marceljsh.binfood.payload.response.TokenResponse;
import com.marceljsh.binfood.repository.UserRepo;
import com.marceljsh.binfood.security.BCrypt;
import com.marceljsh.binfood.service.spec.AuthService;
import com.marceljsh.binfood.service.spec.ValidationService;
import com.marceljsh.binfood.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DefaultAuthService implements AuthService {

  private final UserRepo userRepo;

  private final ValidationService validationService;

  @Autowired
  public DefaultAuthService(UserRepo userRepo, ValidationService validationService) {
    this.userRepo = userRepo;
    this.validationService = validationService;
  }

  private Long next30minutes() {
    long thirtyMinutesInMillis = 1800000;
    return System.currentTimeMillis() + thirtyMinutesInMillis;
  }

  @Override
  @Transactional
  public TokenResponse login(UserLoginRequest request) {
    validationService.validate(request);

    User user = userRepo.findFirstByUsername(request.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "incorrect username or password"));

    if (user.getToken() != null && user.getTokenExpiredAt() > System.currentTimeMillis()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user already logged in");
    }

    if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "incorrect username or password");
    }

    user.setToken(TokenGenerator.generateRandomToken());
    user.setTokenExpiredAt(next30minutes());
    userRepo.save(user);

    return TokenResponse.builder()
        .token(user.getToken())
        .expiredAt(user.getTokenExpiredAt())
        .build();
  }

  @Override
  @Transactional
  public void logout(User user) {
    user.setToken(null);
    user.setTokenExpiredAt(null);

    userRepo.save(user);
  }
}
