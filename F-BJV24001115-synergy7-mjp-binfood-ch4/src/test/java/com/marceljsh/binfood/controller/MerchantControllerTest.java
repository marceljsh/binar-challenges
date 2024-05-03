package com.marceljsh.binfood.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marceljsh.binfood.model.Merchant;
import com.marceljsh.binfood.model.User;
import com.marceljsh.binfood.payload.response.ApiResponse;
import com.marceljsh.binfood.payload.response.MerchantResponse;
import com.marceljsh.binfood.repository.MerchantRepo;
import com.marceljsh.binfood.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class MerchantControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MerchantRepo merchantRepo;

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    userRepo.deleteAll();
    merchantRepo.deleteAll();
  }

  void birth() {
    userRepo.save(User.builder()
      .username("kdot")
      .email("kdto@tde.com")
      .password("password")
      .token("my-token")
      .tokenExpiredAt(System.currentTimeMillis() + 1800000)
      .build());
  }

  void seed() {
    merchantRepo.save(Merchant.builder()
        .name("Walter White")
        .location("Albuquerque")
        .open(true)
        .build());

    merchantRepo.save(Merchant.builder()
        .name("Gus Fring")
        .location("Los Pollos Hermanos")
        .open(true)
        .build());

    merchantRepo.save(Merchant.builder()
        .name("Eazy-E")
        .location("Compton")
        .open(false)
        .build());
  }

  @Test
  void getAllSuccess() throws Exception {
    birth();
    seed();

    mockMvc.perform(
      get("/merchants")
        .header("Authorization", "Bearer my-token")
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      ApiResponse<List<MerchantResponse>> response = mapper.readValue(
        result.getResponse().getContentAsString(),
        new TypeReference<>() {}
      );

      assertNull(response.getError());
      assertNotNull(response.getData());
      assertEquals(3, response.getData().size());
    });
  }

  @Test
  void getAllUnauthorized() throws Exception {
    mockMvc.perform(
      get("/merchants")
        .header("Authorization", "Bearer my-token")
    ).andExpectAll(
      status().isUnauthorized()
    );
  }

  @Test
  void getAllEmpty() throws Exception {
    birth();

    mockMvc.perform(
      get("/merchants")
        .header("Authorization", "Bearer my-token")
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      ApiResponse<List<MerchantResponse>> response = mapper.readValue(
        result.getResponse().getContentAsString(),
        new TypeReference<>() {}
      );

      assertNull(response.getError());
      assertNotNull(response.getData());
      assertEquals(0, response.getData().size());
    });
  }
}