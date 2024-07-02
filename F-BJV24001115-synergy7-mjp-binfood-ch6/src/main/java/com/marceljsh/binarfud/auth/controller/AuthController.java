package com.marceljsh.binarfud.auth.controller;

import com.marceljsh.binarfud.auth.dto.AuthResponse;
import com.marceljsh.binarfud.auth.service.AuthService;
import com.marceljsh.binarfud.auth.dto.LoginRequest;
import com.marceljsh.binarfud.auth.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthService authService;

  @PostMapping(
    value = "/sign-up",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<AuthResponse> signUp(@RequestBody RegisterRequest request) {
    log.info("Received sign-up request, email: {}", request.getEmail());

    AuthResponse response = authService.register(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping(
    value = "/sign-in",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest request) {
    log.info("Received sign-in request for user @{}", request.getUsername());

    AuthResponse response = authService.authenticate(request);

    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/sign-out")
  public ResponseEntity<Map<String, String>> signOut(HttpServletRequest request) {
    log.info("Received sign-out request");

    authService.logout(request);

    return ResponseEntity.ok().build();
  }

}
