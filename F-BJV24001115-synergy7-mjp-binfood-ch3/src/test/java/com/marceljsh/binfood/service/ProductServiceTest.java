package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.dto.CreateMerchantRequest;
import com.marceljsh.binfood.model.dto.CreateProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

  private final ProductService productService = ProductService.getInstance();
  private final MerchantService merchantService = MerchantService.getInstance();

  @BeforeEach
  void setUp() {
    productService.clear();
    merchantService.clear();
  }

  UUID seedAndGetIdOfMerchant() {
    CreateMerchantRequest merchantRequest = CreateMerchantRequest.builder()
        .name("Warung Makan Sederhana")
        .location("Jl. Raya Sederhana")
        .build();
    merchantService.save(merchantRequest);

    return merchantService.findAll().get(0).getId();
  }

  @Test
  void saveSuccess() {
    UUID merchantId = seedAndGetIdOfMerchant();
    CreateProductRequest request = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build();

    boolean result = productService.save(request);

    assertTrue(result);
  }

  @Test
  void saveFailedEmptyName() {
    UUID merchantId = seedAndGetIdOfMerchant();
    CreateProductRequest request = CreateProductRequest.builder()
        .name("")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build();

    boolean result = productService.save(request);

    assertFalse(result);
  }

  @Test
  void saveFailedInvalidPrice() {
    UUID merchantId = seedAndGetIdOfMerchant();
    CreateProductRequest request = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(0L)
        .merchantId(merchantId.toString())
        .build();

    boolean result = productService.save(request);

    assertFalse(result);
  }

  @Test
  void saveFailedInvalidMerchantId() {
    CreateProductRequest request = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId("123")
        .build();

    boolean result = productService.save(request);

    assertFalse(result);
  }

  @Test
  void saveFailedMerchantNotFound() {
    UUID merchantId = UUID.randomUUID();
    CreateProductRequest request = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build();

    boolean result = productService.save(request);

    assertFalse(result);
  }

  @Test
  void findAllPopulated() {
    UUID merchantId = seedAndGetIdOfMerchant();
    productService.save(CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build());
    productService.save(CreateProductRequest.builder()
        .name("Mie Goreng")
        .price(12000L)
        .merchantId(merchantId.toString())
        .build());

    assertEquals(2, productService.size());
  }

  @Test
  void findAllUnpopulated() {
    assertEquals(0, productService.size());
  }

  @Test
  void findByIdSuccess() {
    UUID merchantId = seedAndGetIdOfMerchant();
    CreateProductRequest productRequest = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build();
    productService.save(productRequest);

    String id = productService.findAll().get(0).getId().toString();

    assertTrue(productService.findById(id).isPresent());
  }

  @Test
  void findByIdNotFound() {
    UUID merchantId = seedAndGetIdOfMerchant();
    CreateProductRequest request = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build();
    productService.save(request);

    String id = UUID.randomUUID().toString();

    assertFalse(productService.findById(id).isPresent());
  }

  @Test
  void findByIdInvalidUUID() {
    assertFalse(productService.findById("123").isPresent());
  }

  @Test
  void findFirstByNameSuccess() {
    UUID merchantId = seedAndGetIdOfMerchant();
    CreateProductRequest request = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build();
    productService.save(request);

    assertTrue(productService.findFirstByName("Nasi Goreng").isPresent());
  }

  @Test
  void findFirstByNameCaseInsensitive() {
    UUID merchantId = seedAndGetIdOfMerchant();
    CreateProductRequest request = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build();
    productService.save(request);

    assertFalse(productService.findFirstByName("nasi goreng").isPresent());
  }

  @Test
  void findFirstByNameNotFound() {
    assertFalse(productService.findFirstByName("Nasi Goreng").isPresent());
  }

  @Test
  void deleteByIdSuccess() {
    UUID merchantId = seedAndGetIdOfMerchant();
    CreateProductRequest request = CreateProductRequest.builder()
        .name("Nasi Goreng")
        .price(15000L)
        .merchantId(merchantId.toString())
        .build();
    boolean result = productService.save(request);

    assertTrue(result);

    String id = productService.findAll().get(0).getId().toString();
    productService.deleteById(id);

    assertEquals(0, productService.size());
  }
}