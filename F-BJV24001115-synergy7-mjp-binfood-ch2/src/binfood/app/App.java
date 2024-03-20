package binfood.app;


import binfood.helper.Styler;
import binfood.model.entity.MenuItem;
import binfood.service.MenuItemService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("SpellCheckingInspection")
public class App {

    private static final int EXIT = 0;
    private static final int CONFIRM_AND_PAY = 99;

    private final MenuItemService menuItemService;

    public App() {
        menuItemService = new MenuItemService();
    }

    public void run() {
        seed();

        Map<MenuItem, Integer> orders = new HashMap<>();

        try (Scanner scanner = new Scanner(System.in)) {
            int choice;
            do {
                mainMenu();
                choice = getUserChoice(scanner);

                if (choice == CONFIRM_AND_PAY) {
                    confirmOrder(orders);
                    choice = handleConfirmation(orders, scanner);
                } else if (isValidMenuItemChoice(choice)) {
                    handleMenuItemChoice(orders, scanner, choice);
                }
            } while (choice != EXIT);
        }
    }

    private void seed() {
        menuItemService.addMenuItem("Nasi Goreng", 13000);
        menuItemService.addMenuItem("Mie Goreng", 13000);
        menuItemService.addMenuItem("Nasi + Ayam", 18000);
        menuItemService.addMenuItem("Es Teh Manis", 3000);
        menuItemService.addMenuItem("Es Jeruk", 5000);
    }

    public void confirmOrder(Map<MenuItem, Integer> orders) {
        System.out.println("=".repeat(26));
        System.out.println("Konfirmasi & Pembayaran");
        System.out.println("=".repeat(26) + "\n");

        System.out.println(invoice(orders));

        System.out.println("\n1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi\n");
    }
    
    private int getUserChoice(Scanner scanner) {
        System.out.print("=> ");
        return scanner.nextInt();
    }

    private boolean isValidMenuItemChoice(int choice) {
        return choice > 0 && choice <= menuItemService.size();
    }

    private void handleMenuItemChoice(Map<MenuItem, Integer> orders, Scanner scanner, int choice) {
        MenuItem menuItem = menuItemService.findById((long) choice);
        int quantity = getQuantityFromUser(scanner, menuItem);
        if (quantity > 0) {
            orders.put(menuItem, quantity);
        }
    }

    private int handleConfirmation(Map<MenuItem, Integer> orders, Scanner scanner) {
        int action = getUserChoice(scanner);
        if (action == 1) {
            generateReceipt(orders);
            return EXIT;
        }
        return action;
    }

    private int getQuantityFromUser(Scanner scanner, MenuItem menuItem) {
        System.out.println("=".repeat(26));
        System.out.println("How many would you like to order?");
        System.out.println("=".repeat(26));
        System.out.printf("%-15s | %s\n", menuItem.getName(), Styler.currency(menuItem.getPrice()));
        System.out.println("(Enter 0 to return)\n");
        System.out.print("qty => ");
        return scanner.nextInt();
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

    public void generateReceipt(Map<MenuItem, Integer> orders) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fileName = String.format("receipt_%s.txt", LocalDateTime.now().format(formatter));
        Path dir = Paths.get("./receipt");

        try {
            Files.createDirectories(dir);

            Path filePath = dir.resolve(fileName);

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                writer.write(receiptHeader());
                writer.write(invoice(orders));
                writer.write(receiptFooter());
            }

        } catch (IOException e) {
            System.err.printf("Error writing to %s: %s%n", fileName, e.getMessage());
        }
    }

    private String receiptHeader() {
        return "=".repeat(26) + "\n" +
                "BinarFud\n" +
                "=".repeat(26) + "\n" +
                "\nTerima kasih sudah memesan" +
                "\ndi BinarFud\n" +
                "\nDibawah ini adalah pesanan anda\n\n";
    }

    private String receiptFooter() {

        return "\nPembayaran : BinarCash\n\n" +
                "=".repeat(26) + "\n" +
                "Simpan struk ini sebagai\n" +
                "bukti pembayaran\n" +
                "=".repeat(26) + "\n";
    }
}
