package com.marceljsh.binfood.controller;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.UserLoginRequest;
import com.marceljsh.binfood.payload.response.ApiResponse;
import com.marceljsh.binfood.payload.response.TokenResponse;
import com.marceljsh.binfood.service.spec.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<TokenResponse> login(@RequestBody UserLoginRequest request) {
    TokenResponse response = authService.login(request);

    return ApiResponse.<TokenResponse>builder().data(response).build();
  }

  @DeleteMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> logout(User user) {
    authService.logout(user);

    return ApiResponse.<String>builder().data("OK").build();
  }
}
