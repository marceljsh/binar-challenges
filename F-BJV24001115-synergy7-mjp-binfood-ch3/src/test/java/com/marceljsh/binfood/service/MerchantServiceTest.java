package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateMerchantRequest;
import com.marceljsh.binfood.model.entity.Merchant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MerchantServiceTest {

  private final MerchantService merchantService = MerchantService.getInstance();

  @BeforeEach
  void setUp() {
    merchantService.clear();
  }

  @Test
  void saveSuccess() {
    CreateMerchantRequest request = CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("Jl. Layang No. 1")
        .build();

    boolean ok = merchantService.save(request);

    assertTrue(ok);
  }

  @Test
  void saveFailedEmptyName() {
    CreateMerchantRequest request = CreateMerchantRequest.builder()
        .name("")
        .location("Jl. Layang No. 1")
        .build();

    boolean ok = merchantService.save(request);

    assertFalse(ok);
  }

  @Test
  void saveFailedEmptyLocation() {
    CreateMerchantRequest request = CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("")
        .build();

    boolean ok = merchantService.save(request);

    assertFalse(ok);
  }

  @Test
  void findAllPopulated() {
    merchantService.save(CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("Jl. Layang No. 1")
        .build());

    merchantService.save(CreateMerchantRequest.builder()
        .name("Mak Itin")
        .location("Jl. Jambu No. 14")
        .build());

    assertEquals(2, merchantService.size());
  }

  @Test
  void findAllUnpopulated() {
    assertEquals(0, merchantService.size());
  }

  @Test
  void findByIdSuccess() {
    CreateMerchantRequest request = CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("Jl. Layang No. 1")
        .build();

    merchantService.save(request);

    Merchant merchant = merchantService.findAll().get(0);

    assertTrue(merchantService.findById(merchant.getId().toString()).isPresent());
  }

  @Test
  void findByIdNotFound() {
    assertFalse(merchantService.findById("123").isPresent());
  }

  @Test
  void findByNamePopulated() {
    merchantService.save(CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("Jl. Layang No. 1")
        .build());

    merchantService.save(CreateMerchantRequest.builder()
        .name("Mak Itin")
        .location("Jl. Jambu No. 14")
        .build());

    assertEquals(2, merchantService.findByName("Mak").size());
  }

  @Test
  void findByNameUnpopulated() {
    assertEquals(0, merchantService.findByName("Mak").size());
  }

  @Test
  void findByLocationPopulated() {
    merchantService.save(CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("Jl. Layang No. 1")
        .build());

    merchantService.save(CreateMerchantRequest.builder()
        .name("Mak Itin")
        .location("Jl. Jambu No. 14")
        .build());

    assertEquals(2, merchantService.findByLocation("Jl").size());
  }

  @Test
  void findByLocationUnpopulated() {
    assertEquals(0, merchantService.findByLocation("Layang").size());
  }

  @Test
  void deleteByIdFound() {
    CreateMerchantRequest request = CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("Jl. Layang No. 1")
        .build();

    merchantService.save(request);

    assertEquals(1, merchantService.size());

    Merchant merchant = merchantService.findAll().get(0);

    merchantService.deleteById(merchant.getId().toString());

    assertEquals(0, merchantService.size());
  }

  @Test
  void deleteByIdNotFound() {
    assertEquals(0, merchantService.size());

    merchantService.deleteById(UUID.randomUUID().toString());

    assertEquals(0, merchantService.size());
  }

  @Test
  void deleteByIdInvalidUUID() {
    assertEquals(0, merchantService.size());

    merchantService.deleteById("123");

    assertEquals(0, merchantService.size());
  }
}