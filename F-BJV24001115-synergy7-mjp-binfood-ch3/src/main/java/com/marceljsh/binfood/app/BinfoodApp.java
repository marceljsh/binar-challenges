package com.marceljsh.binfood.app;

import com.marceljsh.binfood.model.dto.CreateMerchantRequest;
import com.marceljsh.binfood.model.dto.CreateProductRequest;
import com.marceljsh.binfood.model.dto.CreateUserRequest;
import com.marceljsh.binfood.service.MerchantService;
import com.marceljsh.binfood.service.ProductService;
import com.marceljsh.binfood.service.UserService;

import java.util.Map;

// TODO: cascade delete
public class BinfoodApp {

  private ProductService productService;

  private UserService userService;

  private MerchantService merchantService;

  public void run() {
    init();

    productService.findAll().forEach(product -> System.out.println(product.getName()));
    userService.findAll().forEach(user -> System.out.println(user.getEmail()));
    merchantService.findAll().forEach(merchant -> System.out.println(merchant.getName()+ "|" + merchant.getLocation())) ;
  }

  public void init() {
    productService = ProductService.getInstance();
    userService = UserService.getInstance();
    merchantService = MerchantService.getInstance();

    seed();
  }

  public void seed() {
    Map.of(
      "Nasi Goreng", 13000L,
      "Mie Goreng", 13000L,
      "Nasi + Ayam", 18000L,
      "Es Teh Manis", 3000L,
      "Es Jeruk", 5000L
    ).forEach((name, price) ->
      productService.save(CreateProductRequest.builder()
        .name(name)
        .price(price)
        .build())
    );

    userService.save(CreateUserRequest.builder()
        .email("johndoe@mail.com")
        .password("secret-password")
        .name("John Doe")
        .build());

    merchantService.save(CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("Jl. Layang No. 1")
        .build());
  }
}
