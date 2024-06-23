package com.marceljsh.binarfud.auth.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class AuthServiceImpl implements AuthService {

  private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private RoleRepository roleRepo;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authManager;

  @Override
  @Transactional
  public AuthResponse register(RegisterRequest request) {
    log.trace("Registering new user @{} <{}>", request.getUsername(), request.getEmail());

    if (userRepo.existsByEmail(request.getEmail())) {
      log.error("Email is already taken");
      throw new DataIntegrityViolationException("Email is already taken");
    }

    if (userRepo.existsByUsername(request.getUsername())) {
      log.error("Username is already taken");
      throw new DataIntegrityViolationException("Username is already taken");
    }

    Set<Role> authorities = new HashSet<>();
    String defaultRole = "ROLE_USER";
    if (roleRepo.existsByName(defaultRole)) {
      Role roleUser = roleRepo.findByName(defaultRole).orElse(null);

      if (roleUser == null) {
        log.error("Role {} not found", defaultRole);
        throw new EntityNotFoundException("Role not found");
      }

      authorities.add(roleUser);
    }

    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(request.getPassword())
        .roles(authorities)
        .build();

    userRepo.save(user);

    String token = jwtService.generateToken(user);
    log.info("User registered: {}", user.getUsername());

    return AuthResponse.of(user, token);
  }

  @Override
  @Transactional
  public AuthResponse authenticate(LoginRequest request) {
    log.trace("Authenticating user @{}", request.getUsername());

    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(), request.getPassword()));

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
