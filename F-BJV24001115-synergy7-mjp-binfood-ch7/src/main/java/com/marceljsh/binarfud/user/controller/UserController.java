package com.marceljsh.binarfud.user.controller;

import com.marceljsh.binarfud.common.dto.PagedResponse;
import com.marceljsh.binarfud.validation.ValidUUID;
import com.marceljsh.binarfud.user.dto.UserResponse;
import com.marceljsh.binarfud.user.dto.UserSearchRequest;
import com.marceljsh.binarfud.user.service.UserService;
import com.marceljsh.binarfud.user.dto.UserUpdateInfoRequest;
import com.marceljsh.binarfud.user.dto.UserChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @GetMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UserResponse> get(@ValidUUID @PathVariable("id") String id) {
    log.info("Received get user request: {}", id);

    UUID userId = UUID.fromString(id);

    UserResponse body = userService.get(userId);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PagedResponse<UserResponse>> search(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    log.info("Received search user request: name={}, email={}, page={}, size={}",
      username, email, page, size);

    UserSearchRequest request = UserSearchRequest.builder()
        .username(username)
        .email(email)
        .page(page - 1)
        .size(size)
        .build();

    Page<UserResponse> result = userService.search(request);
    PagedResponse<UserResponse> body = PagedResponse.from(result);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @PutMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<UserResponse> update(@ValidUUID @PathVariable("id") String id,
      @RequestBody UserUpdateInfoRequest request) {
    log.info("Received update user info request: id={}", id);

    UUID userId = UUID.fromString(id);
    request.setId(userId);

    UserResponse body = userService.updateInfo(request);

    return ResponseEntity.ok(body);
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @PatchMapping(
    value = "/{id}/change-password",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> changePassword(@ValidUUID @PathVariable("id") String id,
      @RequestBody UserChangePasswordRequest request) {
    log.info("Received change password request: id={}", id);

    UUID userId = UUID.fromString(id);
    request.setId(userId);

    userService.updatePassword(request);

    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @DeleteMapping(
    value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> activate(@ValidUUID @PathVariable("id") String id) {
    log.info("Received activate user request: id={}", id);

    UUID userId = UUID.fromString(id);

    userService.deactivate(userId);

    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GOD')")
  @PatchMapping(
    value = "/{id}/restore",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> restore(@ValidUUID @PathVariable("id") String id) {
    log.info("Received restore user request: id={}", id);

    UUID userId = UUID.fromString(id);

    userService.restore(userId);

    return ResponseEntity.ok().build();
  }

}
