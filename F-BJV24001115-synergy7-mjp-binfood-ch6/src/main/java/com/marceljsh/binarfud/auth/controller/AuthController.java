package com.marceljsh.binarfud.auth.controller;

import com.marceljsh.binarfud.auth.dto.AuthResponse;
import com.marceljsh.binarfud.auth.service.AuthService;
import com.marceljsh.binarfud.auth.dto.LoginRequest;
import com.marceljsh.binarfud.auth.dto.RegisterRequest;
import com.marceljsh.binarfud.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthService authService;

  private final UserService userService;

  private final AuthenticationManager authManager;

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

  @RequestMapping("/oauth2-register")
  public Principal user(Principal principal) {
    return principal;
  }

  @GetMapping(
    value = "/google",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> oauth2Success(Authentication auth) {
    OidcUser oidcUser = (OidcUser) auth.getPrincipal();

//    if (userService.)

    return null;
  }

  @PostMapping(
    value = "/sign-in",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest request) {
    log.info("Received sign-in request for user @{}", request.getUsername());

    authManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getUsername(), request.getPassword()));

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
