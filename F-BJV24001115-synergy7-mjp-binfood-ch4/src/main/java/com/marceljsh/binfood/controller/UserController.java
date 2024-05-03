package com.marceljsh.binfood.controller;

import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.UserRegisterRequest;
import com.marceljsh.binfood.payload.request.UserUpdateInfoRequest;
import com.marceljsh.binfood.payload.request.UserUpdatePasswordRequest;
import com.marceljsh.binfood.payload.response.ApiResponse;
import com.marceljsh.binfood.payload.response.UserResponse;
import com.marceljsh.binfood.service.spec.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<UserResponse> register(@RequestBody UserRegisterRequest request) {
    UserResponse response = userService.register(request);
    return ApiResponse.<UserResponse>builder().data(response).build();
  }

  @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<UserResponse> get(User user) {
    UserResponse response = userService.get(user);
    return ApiResponse.<UserResponse>builder().data(response).build();
  }

  @PutMapping(path = "/current/alter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<UserResponse> update(User user, @RequestBody UserUpdateInfoRequest request) {
    UserResponse response = userService.update(user, request);
    return ApiResponse.<UserResponse>builder().data(response).build();
  }

  @PutMapping(path = "/current/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> changePassword(User user, @RequestBody UserUpdatePasswordRequest request) {
    String response = userService.changePassword(user, request);
    return ApiResponse.<String>builder().data(response).build();
  }

  @DeleteMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse<String> delete(User user) {
    userService.softDelete(user);
    return ApiResponse.<String>builder().data("OK").build();
  }
}
