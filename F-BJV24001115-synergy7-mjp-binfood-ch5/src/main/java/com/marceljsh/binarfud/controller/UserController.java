package com.marceljsh.binarfud.controller;

import com.marceljsh.binarfud.payload.request.UserChangePasswordRequest;
import com.marceljsh.binarfud.payload.request.UserRegisterRequest;
import com.marceljsh.binarfud.payload.request.UserSearchRequest;
import com.marceljsh.binarfud.payload.request.UserUpdateInfoRequest;
import com.marceljsh.binarfud.payload.response.PagedResponse;
import com.marceljsh.binarfud.payload.response.UserResponse;
import com.marceljsh.binarfud.service.spec.UserService;
import com.marceljsh.binarfud.util.Constants;
import com.marceljsh.binarfud.validation.annotation.ValidUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(
    path = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UserResponse> get(@ValidUUID @PathVariable("id") String id) {
    log.info("finding user {}", id);

    UUID userId = UUID.fromString(id);
    UserResponse response = userService.get(userId);
    log.info("user {} found", id);

    return ResponseEntity.ok(response);
  }

  @GetMapping(
    path = "/@{username}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UserResponse> getByUsername(@PathVariable("username") String username) {
    log.info("finding user by username {}", username);

    UserResponse response = userService.findByUsername(username);
    log.info("user {} found", username);

    return ResponseEntity.ok(response);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PagedResponse<UserResponse>> search(
    @RequestParam(value = "username", required = false) String username,
    @RequestParam(value = "email", required = false) String email,
    @RequestParam(value = "active", required = false) Boolean active,
    @RequestParam(value = "page", defaultValue = "1") int page,
    @RequestParam(value = "size", defaultValue = "10") int size) {

    log.info("searching users with [username: {} | email: {} | active: {} | page: {} | size: {}]", username, email, active, page, size);

    UserSearchRequest request = UserSearchRequest.builder()
        .username(username)
        .email(email)
        .page(page - 1)
        .size(size)
        .build();

    Page<UserResponse> result = userService.search(request);
    PagedResponse<UserResponse> response = PagedResponse.of("users", result);

    return ResponseEntity.ok(response);
  }

  @PutMapping(
    path = "/{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UserResponse> updateInfo(@ValidUUID @PathVariable("id") String id, @RequestBody UserUpdateInfoRequest request) {
    log.info("updating user info {}", id);

    request.setId(id);

    UserResponse response = userService.updateInfo(request);
    log.info("user {} info updated", id);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping(
    path = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> deactivate(@ValidUUID @PathVariable("id") String id) {
    log.info("deactivating user {}", id);

    UUID userId = UUID.fromString(id);
    userService.deactivate(userId);
    log.info("user {} deactivated", id);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }

  @PatchMapping(
    path = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> restore(@ValidUUID @PathVariable("id") String id) {
    log.info("restoring user {}", id);

    UUID userId = UUID.fromString(id);
    userService.restore(userId);
    log.info("user {} restored", id);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }

  @PatchMapping(
    path = "/{id}/change-password",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Map<String, String>> changePassword(@ValidUUID @PathVariable("id") String id, @RequestBody UserChangePasswordRequest request) {
    log.info("updating password for user {}", id);

    request.setId(id);

    if (!request.getPassword().equals(request.getConfirmPassword())) {
      String msg = "passwords do not match";
      log.error(msg);
      throw new IllegalArgumentException(msg);
    }

    userService.updatePassword(request);

    log.info("password updated for user {}", id);

    return ResponseEntity.ok(Constants.OK_RESPONSE);
  }
}
