package com.marceljsh.binfood.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.request.UserLoginRequest;
import com.marceljsh.binfood.payload.response.ApiResponse;
import com.marceljsh.binfood.payload.response.TokenResponse;
import com.marceljsh.binfood.repository.UserRepo;
import com.marceljsh.binfood.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    userRepo.deleteAll();
  }

  @Test
  void loginSuccess() throws Exception {
    userRepo.save(User.builder()
        .username("coleworld")
        .email("cole@dreamville.com")
        .password(BCrypt.hashpw("wedabigthree", BCrypt.gensalt()))
        .build());

    UserLoginRequest request = UserLoginRequest.builder()
      .username("coleworld")
      .password("wedabigthree")
      .build();

    mockMvc.perform(
      post("/auth/login")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request))
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      ApiResponse<TokenResponse> response = mapper.readValue(
        result.getResponse().getContentAsString(),
        new TypeReference<>() {}
      );

      assertNull(response.getError());
      assertNotNull(response.getData());
    });
  }

  @Test
  void logoutSuccess() throws Exception {
    userRepo.save(User.builder()
      .username("coleworld")
      .email("cole@dreamville.com")
      .password(BCrypt.hashpw("wedabigthree", BCrypt.gensalt()))
      .build());

    UserLoginRequest request = UserLoginRequest.builder()
      .username("coleworld")
      .password("wedabigthree")
      .build();

    mockMvc.perform(
      post("/auth/login")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request))
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      ApiResponse<TokenResponse> response = mapper.readValue(
        result.getResponse().getContentAsString(),
        new TypeReference<>() {}
      );

      assertNull(response.getError());
      assertNotNull(response.getData());
    });

    mockMvc.perform(
      delete("/auth/logout")
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      ApiResponse<String> response = mapper.readValue(
        result.getResponse().getContentAsString(),
        new TypeReference<>() {}
      );

      assertNull(response.getError());
    });
  }
}