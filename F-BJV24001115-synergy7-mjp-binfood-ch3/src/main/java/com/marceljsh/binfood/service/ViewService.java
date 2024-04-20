package com.marceljsh.binfood.service;

import com.marceljsh.binfood.model.entity.OrderDetail;
import com.marceljsh.binfood.model.entity.Product;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ViewService {

  private ViewService() {
  }

  public static String input(Scanner scanner, String prefix) {
    System.out.printf("%n%s => ", prefix);
    return scanner.nextLine();
  }

  public static String input(Scanner scanner) {
    System.out.print("\n=> ");
    return scanner.nextLine();
  }

  private static String formatNumber(long number) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    symbols.setGroupingSeparator('.');

    DecimalFormat formatter = new DecimalFormat("#,###", symbols);

    return formatter.format(number);
  }

  private static String productString(Product product) {
    return String.format("%-15s | %-12s", product.getName(), formatNumber(product.getPrice()));
  }

  private static String border(String... messages) {
    StringBuilder sb = new StringBuilder();
    final int BAR_LEN = 32;

    sb.append("=".repeat(BAR_LEN)).append("\n");
    for (String msg : messages) {
      sb.append(msg).append("\n");
    }
    sb.append("=".repeat(BAR_LEN));

    return sb.toString();
  }

  private static String billOfMaterial(List<OrderDetail> orderDetails) {
    StringBuilder sb = new StringBuilder();
    int totalQuantity = 0;
    long totalCost = 0L;

    for (OrderDetail orderDetail : orderDetails) {
      Product product = orderDetail.getProduct();
      int quantity = orderDetail.getQuantity();
      totalQuantity += quantity;
      long subtotal = product.getPrice() * quantity;
      totalCost += subtotal;
      sb.append(String.format("%-15s %3d %15s%n", product.getName(), quantity, formatNumber(subtotal)));
    }

    sb.append("-".repeat(35))
        .append("+\n")
        .append(String.format("%-15s %3d %15s", "Total", totalQuantity, formatNumber(totalCost)));

    return sb.toString();
  }

  public static String receipt(List<OrderDetail> orderDetails) {
    StringBuilder sb = new StringBuilder();

    sb.append(border("BinarFud"))
        .append("\nTerima kasih sudah memesan\ndi BinarFud\n")
        .append("\nDi bawah ini adalah pesanan anda\n\n")
        .append(billOfMaterial(orderDetails))
        .append("\n\nPembayaran : BinarCash\n\n")
        .append(border("Simpan struk ini sebagai", "bukti pembayaran"));

    return sb.toString();
  }

  public static void mainMenuView(List<Product> products) {
    System.out.println(border("Selamat datang di BinarFud"));
    System.out.println("\nSilahkan pilih makanan:");

    for (int i = 0; i < products.size(); i++) {
      Product product = products.get(i);
      System.out.printf("%d. %s%n", i + 1, productString(product));
    }
    System.out.println("99. Pesan dan Bayar");
    System.out.println("0. Keluar aplikasi");
  }

  public static void quantityView(Product product) {
    System.out.println(border("Berapa pesanan anda"));
    System.out.println("\n" + productString(product));
    System.out.println("(input 0 untuk kembali)");
  }

  public static void paymentView(List<OrderDetail> orderDetails) {
    System.out.println(border("Konfirmasi & Pembayaran"));
    System.out.println();

    System.out.println(billOfMaterial(orderDetails) + "\n");

    System.out.println("1. Konfirmasi dan bayar");
    System.out.println("2. Kembali ke menu utama");
    System.out.println("0. Keluar aplikasi");
  }

  public static void newSessionView() {
    System.out.println(border("Mohon masukkan input", "pilihan anda"));
    System.out.println("(Y) untuk lanjut");
    System.out.println("(N) untuk keluar");
  }

  public static void emptyCartView() {
    System.out.println(border("Minimal 1 jumlah", "pesanan!"));
  }
}
