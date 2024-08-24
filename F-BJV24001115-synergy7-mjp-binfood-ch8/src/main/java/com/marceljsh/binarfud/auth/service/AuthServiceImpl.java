package com.marceljsh.binarfud.auth.service;

import com.marceljsh.binarfud.app.util.Constants;
import com.marceljsh.binarfud.auth.dto.AuthResponse;
import com.marceljsh.binarfud.auth.dto.LoginRequest;
import com.marceljsh.binarfud.auth.dto.RegisterRequest;
import com.marceljsh.binarfud.security.service.JwtService;
import com.marceljsh.binarfud.security.model.Role;
import com.marceljsh.binarfud.security.repository.RoleRepository;
import com.marceljsh.binarfud.user.model.User;
import com.marceljsh.binarfud.user.repositoy.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

  private final UserRepository userRepo;

  private final RoleRepository roleRepo;

  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  @Value("${god.password}")
  private String godPassword;

  @Override
  @Transactional
  public void addGod() {
    String username = "god";
    String email = "god@this.com";

    if (!userRepo.existsByEmailOrUsername(email, username)) {
      Role roleGod = roleRepo.findByName("ROLE_GOD")
          .orElse(roleRepo.findAll().get(0));

      User god = User.builder()
          .username(username)
          .email(email)
          .password(passwordEncoder.encode(godPassword))
          .roles(Set.of(roleGod))
          .build();

      userRepo.save(god);
    }

    log.info("God is here");
  }

  @Override
  @Transactional
  public void registerOAuth2(String email) {
    if (userRepo.existsByEmail(email)) {
      log.error(Constants.MSG_EMAIL_TAKEN);
      throw new DataIntegrityViolationException(Constants.MSG_EMAIL_TAKEN);
    }

    String defaultRole = "ROLE_USER";
    Role roleUser = roleRepo.findByName(defaultRole).orElse(null);
    if (roleUser == null) {
      log.error(Constants.MSG_ROLE_NOT_FOUND);
      throw new EntityNotFoundException(Constants.MSG_ROLE_NOT_FOUND);
    }

    String username = email.split("@")[0] + "_oauth2";
    User user = User.builder()
        .username(username)
        .email(email)
        .roles(Set.of(roleUser))
        .build();

    userRepo.save(user);

    log.info("OAuth2 User registered: {}", user.getEmail());
  }

  @Override
  @Transactional
  public AuthResponse register(RegisterRequest request) {
    log.trace("Registering new user @{} <{}>", request.getUsername(), request.getEmail());

    if (userRepo.existsByEmail(request.getEmail())) {
      log.error(Constants.MSG_EMAIL_TAKEN);
      throw new DataIntegrityViolationException(Constants.MSG_EMAIL_TAKEN);
    }

    if (request.getUsername().endsWith("_oauth2")) {
      log.error("Username cannot end with '_oauth2'");
      throw new DataIntegrityViolationException("Username cannot end with '_oauth2'");
    }

    if (userRepo.existsByUsername(request.getUsername())) {
      log.error(Constants.MSG_USERNAME_TAKEN);
      throw new DataIntegrityViolationException(Constants.MSG_USERNAME_TAKEN);
    }

    Set<Role> authorities = new HashSet<>();
    String defaultRole = "ROLE_USER";
    if (roleRepo.existsByName(defaultRole)) {
      Role roleUser = roleRepo.findByName(defaultRole).orElse(null);

      if (roleUser == null) {
        log.error("Role {} not found", defaultRole);
        throw new EntityNotFoundException(Constants.MSG_ROLE_NOT_FOUND);
      }

      authorities.add(roleUser);
    }

    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .roles(authorities)
        .build();

    userRepo.save(user);

    String token = jwtService.generateToken(user);
    log.info("User registered: @{}", user.getUsername());

    return AuthResponse.of(user, token);
  }

  @Override
  @Transactional
  public AuthResponse authenticate(LoginRequest request) {
    log.trace("Authenticating user @{}", request.getUsername());

    User user = userRepo.findByUsername(request.getUsername()).orElse(null);
    if (user == null) {
      log.error("User @{} not found", request.getUsername());
      throw new EntityNotFoundException("User not found");
    }

    String token = jwtService.generateToken(user);

    log.info("User @{} authenticated successfully", user.getUsername());

    return AuthResponse.of(user, token);
  }

  @Override
  @Transactional
  public void logout(HttpServletRequest request) {
    String token = request.getHeader("Authorization").substring(7);
    String username = jwtService.extractUsername(token);
    log.trace("Logging out user @{}", username);

    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    SecurityContextHolder.clearContext();

    log.info("User @{} logged out successfully", username);
  }

}
