package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.model.Role;
import com.marceljsh.binarfud.model.User;
import com.marceljsh.binarfud.payload.request.UserLoginRequest;
import com.marceljsh.binarfud.payload.request.UserRegisterRequest;
import com.marceljsh.binarfud.payload.response.AuthResponse;
import com.marceljsh.binarfud.repository.UserRepository;
import com.marceljsh.binarfud.service.spec.AuthService;
import com.marceljsh.binarfud.service.spec.BlacklistService;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthServiceImpl implements AuthService {

  private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtServiceImpl jwtServiceImpl;

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private BlacklistService blacklistService;

  @Transactional
  @Override
  public AuthResponse register(UserRegisterRequest request) {
    if (userRepo.existsByEmail(request.getEmail())) {
      log.error("email already exists");
      throw new DataIntegrityViolationException("email already exists");
    }

    if (userRepo.existsByUsername(request.getUsername())) {
      log.error("username already exists");
      throw new DataIntegrityViolationException("username already exists");
    }

    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.ROLE_CUSTOMER)
        .build();

    User other = userRepo.save(user);
    if (!user.equals(other)) {
      log.error("failed to register");
      throw new TransactionSystemException("failed to register");
    }

    log.info("user @{} saved", request.getUsername());
    String token = jwtServiceImpl.generateToken(user);
    log.info("token generated for user @{}", request.getUsername());

    return AuthResponse.builder()
        .token(token)
        .build();
  }

  @Transactional
  @Override
  public AuthResponse authenticate(UserLoginRequest request) {
    authManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getUsername(), request.getPassword()
      )
    );

    User user = userRepo.findByUsername(request.getUsername())
        .orElseThrow();
    String token = jwtServiceImpl.generateToken(user);

    return AuthResponse.builder()
        .token(token)
        .build();
  }

  @Transactional
  @Override
  public void signOut(String token) {
    String username = jwtServiceImpl.extractUsername(token);
    blacklistService.addToBlacklist(token);
    log.debug("user @{} signed out", username);
  }
}
