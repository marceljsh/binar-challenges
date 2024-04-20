package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

  private final UserService userService = UserService.getInstance();

  @BeforeEach
  void setUp() {
    userService.clear();
  }

  @Test
  void saveSuccess() {
    CreateUserRequest request = CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdot@tde.com")
      .password("itsjustbigme")
      .build();

    boolean ok = userService.save(request);

    assertTrue(ok);
  }

  @Test
  void saveFailedEmptyName() {
    CreateUserRequest request = CreateUserRequest.builder()
      .name("")
      .email("kdot@tde.com")
      .password("itsjustbigme")
      .build();

    boolean ok = userService.save(request);

    assertFalse(ok);
  }

  @Test
  void saveFailedEmptyEmail() {
    CreateUserRequest request = CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("")
      .password("itsjustbigme")
      .build();

    boolean ok = userService.save(request);

    assertFalse(ok);
  }

  @Test
  void saveFailedEmptyPassword() {
    CreateUserRequest request = CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdot@tde.com")
      .password("")
      .build();

    boolean ok = userService.save(request);

    assertFalse(ok);
  }

  @Test
  void saveFailedInvalidEmail() {
    CreateUserRequest request = CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdottdecom")
      .password("itsjustbigme")
      .build();

    boolean ok = userService.save(request);

    assertFalse(ok);
  }

  @Test
  void clear() {
    userService.save(CreateUserRequest.builder()
      .name("John Doe")
      .email("johndoe@npc.com")
      .password("secret")
      .build());

    userService.save(CreateUserRequest.builder()
      .name("Jane Doe")
      .email("janedoe@npc.com")
      .password("secret")
      .build());

    assertEquals(2, userService.size());

    userService.clear();

    assertEquals(0, userService.size());
  }

  @Test
  void deleteByIdSuccess() {
    userService.save(CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdot@tde.com")
      .password("itsjustbigme")
      .build());

    assertEquals(1, userService.size());

    String id = userService.findAll().get(0).getId().toString();

    userService.deleteById(id);

    assertEquals(0, userService.size());
  }

  @Test
  void deleteByIdNotFound() {
    userService.save(CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdot@tde.com")
      .password("itsjustbigme")
      .build());

    assertEquals(1, userService.size());

    String id = "123";

    userService.deleteById(id);

    assertEquals(1, userService.size());
  }

  @Test
  void findAllPopulated() {
    userService.save(CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdot@tde.com")
      .password("itsjustbigme")
      .build());

    assertEquals(1, userService.size());
  }

  @Test
  void findAllUnpopulated() {
    assertEquals(0, userService.size());
  }

  @Test
  void findByIdSuccess() {
    userService.save(CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdot@tde.com")
      .password("itsjustbigme")
      .build());

    String id = userService.findAll().get(0).getId().toString();

    assertTrue(userService.findById(id).isPresent());
  }

  @Test
  void findByIdNotFound() {
    assertFalse(userService.findById("123").isPresent());
  }

  @Test
  void findFirstByEmailSuccess() {
    userService.save(CreateUserRequest.builder()
      .name("Kendrick Lamar")
      .email("kdot@tde.com")
      .password("itsjustbigme")
      .build());

    assertTrue(userService.findFirstByEmail("kdot@tde.com").isPresent());
  }

  @Test
  void findFirstByEmailNotFound() {
    assertFalse(userService.findFirstByEmail("kdot@tde.com").isPresent());
  }

  @Test
  void findByNameFound() {
    userService.save(CreateUserRequest.builder()
      .name("John Doe")
      .email("johndoe@npc.com")
      .password("secret")
      .build());

    userService.save(CreateUserRequest.builder()
      .name("Jane Doe")
      .email("janedoe@npc.com")
      .password("secret")
      .build());

    assertEquals(2, userService.findByName("Doe").size());
  }

  @Test
  void findByNameNotFound() {
    assertEquals(0, userService.findByName("Doe").size());
  }
}