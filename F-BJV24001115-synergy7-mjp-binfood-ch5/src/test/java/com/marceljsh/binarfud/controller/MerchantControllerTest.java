package com.marceljsh.binarfud.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marceljsh.binarfud.payload.request.MerchantAddRequest;
import com.marceljsh.binarfud.payload.response.ErrorResponse;
import com.marceljsh.binarfud.payload.response.MerchantResponse;
import com.marceljsh.binarfud.repository.MerchantRepository;
import com.marceljsh.binarfud.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MerchantControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MerchantRepository merchantRepo;

  @Autowired
  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    merchantRepo.deleteAll();
  }

  @Test
  void addSuccess() throws Exception {
    MerchantAddRequest request = MerchantAddRequest.builder()
        .name("Los Pollos Hermanos")
        .location("Albuquerque")
        .build();

    Pattern pattern = Pattern.compile(Constants.Rgx.DISPLAY_NAME);
    Matcher matcher = pattern.matcher(request.getName());
    System.out.println("\n".repeat(10));
    System.out.println(matcher.matches());
    System.out.println("\n".repeat(10));

    mockMvc.perform(
        post("/merchants")
            .accept(MediaType.APPLICATION_JSON)
            .contentType("application/json")
            .content(mapper.writeValueAsString(request)))
        .andExpectAll(
            status().isOk())
        .andDo(result -> {
          MerchantResponse response = mapper.readValue(result.getResponse().getContentAsString(),
              new TypeReference<>() {
              });

          assertNotNull(response.getId());
          assertEquals(request.getName(), response.getName());
          assertEquals(request.getLocation(), response.getLocation());
          assertNotNull(response.getCreatedAt());
          assertNotNull(response.getUpdatedAt());
        });
  }

  @Test
  void addFailed_givenEmptyName() throws Exception {
    MerchantAddRequest request = MerchantAddRequest.builder()
        .name("")
        .location("Albuquerque")
        .build();

    mockMvc.perform(
        post("/merchants")
            .accept(MediaType.APPLICATION_JSON)
            .contentType("application/json")
            .content(mapper.writeValueAsString(request)))
        .andExpectAll(
            status().isBadRequest())
        .andDo(result -> {
          ErrorResponse response = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
          });

          assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
          assertNotNull(response.getMessage());
          assertTrue(response.getMessage().contains("name cannot be empty"));
        });
  }

  @Test
  void addFailed_givenInvalidName() throws Exception {
    MerchantAddRequest request = MerchantAddRequest.builder()
        .name("Los@Pollos@Hermanos@123")
        .location("Albuquerque")
        .build();

    mockMvc.perform(
        post("/merchants")
            .accept(MediaType.APPLICATION_JSON)
            .contentType("application/json")
            .content(mapper.writeValueAsString(request)))
        .andExpectAll(
            status().isBadRequest())
        .andDo(result -> {
          ErrorResponse response = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
          });

          assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
          assertNotNull(response.getMessage());
          assertTrue(response.getMessage().contains("name can only have alphanumeric characters and spaces"));
        });
  }
}