package com.marceljsh.binarfud.controller;

import com.marceljsh.binarfud.payload.request.UserLoginRequest;
import com.marceljsh.binarfud.payload.request.UserRegisterRequest;
import com.marceljsh.binarfud.payload.response.AuthResponse;
import com.marceljsh.binarfud.service.spec.AuthService;
import com.marceljsh.binarfud.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  private final Logger log = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(
    value = "/sign-up",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<AuthResponse> register(@RequestBody UserRegisterRequest request) {
    log.trace("registering user @{}", request.getUsername());
    log.trace("{}", request);

    AuthResponse response = authService.register(request);
    log.info("user @{} registered", request.getUsername());

    return ResponseEntity.ok(response);
  }

  @PostMapping(
    value = "/sign-in",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest request) {
    log.trace("logging in user @{}", request.getUsername());
    log.trace("{}", request);

    AuthResponse response = authService.authenticate(request);
    log.info("user @{} logged in", request.getUsername());

    return ResponseEntity.ok(response);
  }

  @PostMapping(
    value = "/sign-out",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new IllegalArgumentException("invalid Authorization");
    }

    String token = authHeader.substring(7);
    if (token.isBlank()) {
      throw new IllegalArgumentException("no token provided");
    }

    authService.signOut(token);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }
}
























