package binfood.app;


import binfood.helper.Styler;
import binfood.model.entity.MenuItem;
import binfood.service.MenuItemService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("SpellCheckingInspection")
public class App {

    private final MenuItemService menuItemService;

    public App() {
        menuItemService = new MenuItemService();
    }

    public void run() {
        seed();

        Map<MenuItem, Integer> orders = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            mainMenu();

            System.out.print("=> ");
            choice = scanner.nextInt();
            System.out.println();

            if (choice == 0)
                break;
            else if (choice == 99) {
                confirmOrders(orders);

                System.out.print("=> ");
                int action = scanner.nextInt();

                if (action == 0)
                    break;
                else if (action == 1) {
                    generateReceipt(orders);
                    break;
                }

            } else if (choice > 0 && choice <= menuItemService.size()) {
                System.out.println("=".repeat(26));
                System.out.println("Berapa pesanan anda");
                System.out.println("=".repeat(26));

                MenuItem menuItem = menuItemService.findById((long) choice);
                System.out.printf("%-15s | %s\n", menuItem.getName(), Styler.currency(menuItem.getPrice()));
                System.out.println("(input 0 untuk kembali)\n");
                System.out.print("qty => ");
                int quantity = scanner.nextInt();
                System.out.println();

                if (quantity > 0) {
                    orders.put(menuItem, quantity);
                }
                /* if quantity is 0, do nothing (automatically goes back to menu) */
            }
        }

        scanner.close();
    }

    private void seed() {
        menuItemService.addMenuItem("Nasi Goreng", 13000);
        menuItemService.addMenuItem("Mie Goreng", 13000);
        menuItemService.addMenuItem("Nasi + Ayam", 18000);
        menuItemService.addMenuItem("Es Teh Manis", 3000);
        menuItemService.addMenuItem("Es Jeruk", 5000);
    }

    private void mainMenu() {
        System.out.println("=".repeat(26));
        System.out.println("Selamat datang di Binarfud");
        System.out.println("=".repeat(26) + "\n");
        System.out.println("Silahkan pilih makanan :");

        int i = 1;
        for (MenuItem menuItem: menuItemService.getAll()) {
            System.out.printf("%d. %-15s | %s\n",
                    i, menuItem.getName(), Styler.currency(menuItem.getPrice()));
            i++;
        }

        System.out.println("99. Pesan dan Bayar");
        System.out.println("0. Keluar aplikasi");
    }

    private String invoice(Map<MenuItem, Integer> orders) {
        StringBuilder invoiceContent = new StringBuilder();
        int total=0, bill=0;

        for (MenuItem menuItem: orders.keySet()) {
            int quantity = orders.get(menuItem);
            int price = menuItem.getPrice();
            bill += price * quantity;
            total += quantity;

            invoiceContent.append(String.format("%-15s %-3d %-10s\n",
                    menuItem.getName(),
                    quantity,
                    Styler.currency(price * quantity)));
        }

        invoiceContent.append("-".repeat(30))
                .append("+\n")
                .append(String.format("%-15s %-3d %-10s\n",
                        "Total", total, Styler.currency(bill)));

        return invoiceContent.toString();
    }

    private void confirmOrders(Map<MenuItem, Integer> orders) {
        System.out.println("=".repeat(26));
        System.out.println("Konfirmasi & Pembayaran");
        System.out.println("=".repeat(26) + "\n");

        System.out.println(invoice(orders));

        System.out.println("\n1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi\n");
    }

    public void generateReceipt(Map<MenuItem, Integer> orders) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date currentDate = new Date();
        String fileName = String.format("receipt_%s.txt",
                formatter.format(currentDate));
        Path dir = Paths.get("./receipt");

        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            Path filePath = dir.resolve(fileName);

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                writer.write("=".repeat(26) + "\n");
                writer.write("BinarFud" + "\n");
                writer.write("=".repeat(26) + "\n");

                writer.write("\nTerima kasih sudah memesan");
                writer.write("\ndi BinarFud\n");
                writer.write("\nDibawah ini adalah pesanan anda\n\n");

                writer.write(invoice(orders));

                writer.write("\nPembayaran : BinarCash\n\n");
                writer.write("=".repeat(26) + "\n");
                writer.write("Simpan struk ini sebagai" + "\n");
                writer.write("bukti pembayaran" + "\n");
                writer.write("=".repeat(26) + "\n");
            }

        } catch (IOException e) {
            System.err.printf("Error writing to %s: %s%n", fileName, e.getMessage());
        }
    }
}
