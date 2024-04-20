package com.marceljsh.binfood.app;

import com.marceljsh.binfood.model.dto.CreateMerchantRequest;
import com.marceljsh.binfood.model.dto.CreateProductRequest;
import com.marceljsh.binfood.model.dto.CreateUserRequest;
import com.marceljsh.binfood.model.entity.OrderDetail;
import com.marceljsh.binfood.model.entity.Product;
import com.marceljsh.binfood.service.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: cascade delete
// TODO: properly use services
// TODO: collect in-memory OrderDetails and bulk save them
public class BinfoodApp {

  private UserService userService;

  private MerchantService merchantService;

  private ProductService productService;

  private OrderService orderService;

  private OrderDetailService orderDetailService;

  public void run() {
    init();

    List<Product> menuList = productService.findAll();
    Map<Integer, Product> menu = IntStream.range(0, menuList.size())
        .boxed()
        .collect(Collectors.toMap(i -> i + 1, menuList::get));

    Scanner scanner = new Scanner(System.in);

    boolean runApp = true;
    List<OrderDetail> orderDetails = new ArrayList<>();
    while (runApp) {
      ViewService.mainMenuView(menuList);
      int selectedItemIdx = Integer.parseInt(ViewService.input(scanner));
      if (selectedItemIdx == 0) {
        System.exit(0);
      } else if (selectedItemIdx == 99) {
        if (orderDetails.isEmpty()) {
          ViewService.emptyCartView();
        } else {
          ViewService.paymentView(orderDetails);
          int choice = Integer.parseInt(ViewService.input(scanner));
          if (choice == 0) {
            System.exit(0);
          } else if (choice == 1) {
            String receipt = ViewService.receipt(orderDetails);
            System.out.println(receipt);
            writeReceipt(receipt);

            ViewService.newSessionView();
            String decision = ViewService.input(scanner);
            if (decision.equalsIgnoreCase("n")) {
              runApp = false;
            } else {
              orderDetails.clear();
            }
          }
        }
      } else {
        ViewService.quantityView(menu.get(selectedItemIdx));
        int quantity = Integer.parseInt(ViewService.input(scanner, "qty"));
        if (quantity > 0) {
          Product product = menu.get(selectedItemIdx);
          orderDetails.add(new OrderDetail(
              UUID.randomUUID(),
              null,
              product,
              quantity,
              product.getPrice() * quantity));
        }
      }
    }

    scanner.close();
  }

  private void init() {
    userService = UserService.getInstance();
    merchantService = MerchantService.getInstance();
    productService = ProductService.getInstance();
    orderService = OrderService.getInstance();
    orderDetailService = OrderDetailService.getInstance();

    seed();
  }

  private void seed() {
    userService.save(CreateUserRequest.builder()
        .email("johndoe@mail.com")
        .password("secret-password")
        .name("John Doe")
        .build());

    merchantService.save(CreateMerchantRequest.builder()
        .name("Mak Robi")
        .location("Jl. Layang No. 1")
        .build());

    String merchantId = merchantService.findAll().get(0).getId().toString();

    Map.of(
        "Nasi Goreng", 15000L,
        "Mie Goreng", 13000L,
        "Nasi + Ayam", 18000L,
        "Es Teh Manis", 3000L,
        "Es Jeruk", 5000L).forEach(
            (name, price) -> productService.save(CreateProductRequest.builder()
                .name(name)
                .price(price)
                .merchantId(merchantId)
                .build()));
  }

  private void writeReceipt(String content) {
    String fileName = String.format("receipt_%s.txt", UUID.randomUUID());

    File directory = new File("./receipts");
    if (!directory.exists()) {
      directory.mkdirs();
    }

    File file = new File(directory, fileName);
    try (BufferedWriter writer = new BufferedWriter((new FileWriter(file)))) {
      writer.write(content);
    } catch (IOException e) {
      System.out.println("Unable to write to file: " + e.getMessage());
    }
  }
}
