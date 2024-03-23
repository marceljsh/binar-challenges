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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("SpellCheckingInspection")
public class App {

    private static final int EXIT = 0;
    private static final int CONFIRM_AND_PAY = 99;

    private final MenuItemService menuItemService;
    private final  Scanner scanner;

    public App() {
        menuItemService = new MenuItemService();
        scanner = new Scanner(System.in);
    }

    public void run() {
        seed();

        boolean newSession;
        do {
            Map<MenuItem, Integer> orders = new HashMap<>();
            int choice;

            do {
                mainMenu();
                choice = getUserChoice();

                if (choice == CONFIRM_AND_PAY) {
                    if (orders.isEmpty()) {
                        String bar = "=".repeat(24);
                        System.out.printf("%s\nMinimal 1 jumlah pesanan%n%s\n", bar, bar);
                        continue;
                    }
                        confirmOrder(orders);
                        choice = handleConfirmation(orders);
                    } else if (isValidMenuItemChoice(choice)) {
                        handleMenuItemChoice(orders, scanner, choice);
                    }
            } while (choice != EXIT);

            newSession = askForNewSession();
        } while (newSession);
    }

    private void seed() {
        menuItemService.addMenuItem("Nasi Goreng", 13000);
        menuItemService.addMenuItem("Mie Goreng", 13000);
        menuItemService.addMenuItem("Nasi + Ayam", 18000);
        menuItemService.addMenuItem("Es Teh Manis", 3000);
        menuItemService.addMenuItem("Es Jeruk", 5000);
    }

    private boolean askForNewSession() {
        Arrays.asList(
                "=".repeat(24),
                "Mohon masukkan input",
                "pilihan anda",
                "=".repeat(24),
                "(Y) untuk lanjut",
                "(n) untuk keluar\n",
                "=> "
        ).forEach(System.out::println);

        String choice = scanner.nextLine();
        return choice.equalsIgnoreCase("y");
    }

    private void confirmOrder(Map<MenuItem, Integer> orders) {
        Arrays.asList(
                "=".repeat(26),
                "Konfirmasi & Pembayaran",
                "=".repeat(26),
                invoice(orders),
                "\n1. Konfirmasi dan Bayar",
                "2. Kembali ke menu utama",
                "0. Keluar aplikasi"
        ).forEach(System.out::println);
    }
    
    private int getUserChoice() {
        System.out.print("=> ");
        while (!scanner.hasNextInt()) {
            // to consume the invalid input
            scanner.next();
            System.out.print("=> ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
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

    private int handleConfirmation(Map<MenuItem, Integer> orders) {
        int action = getUserChoice();
        if (action == 1) {
            generateReceipt(orders);
            return EXIT;
        }
        return action;
    }

    private int getQuantityFromUser(Scanner scanner, MenuItem menuItem) {
        Arrays.asList(
                "=".repeat(26) + "\n",
                "How many would you like to order?\n",
                "=".repeat(26) + "\n",
                String.format("%-15s | %s\n", menuItem.getName(),
                        Styler.currency(menuItem.getPrice())),
                "(Enter 0 to return)\n\n",
                "qty => "
        ).forEach(System.out::print);

        while (!scanner.hasNextInt()) {
            // consume the invalid input
            scanner.next();
            System.out.print("qty => ");
        }
        int quantity = scanner.nextInt();

        // consume the newline character left by nextInt()
        scanner.nextLine();
        return quantity;
    }

    private void mainMenu() {
        String bar = "=".repeat(26);
        Arrays.asList(
                bar,
                "Selamat datang di Binarfud",
                bar + "\n",
                "Silahkan pilih makanan :"
        ).forEach(System.out::println);

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
        int total = 0;
        int bill = 0;

        for (Map.Entry<MenuItem, Integer> entry: orders.entrySet()) {
            String itemName = entry.getKey().getName();
            int quantity = entry.getValue();
            int price = entry.getKey().getPrice();
            bill += price * quantity;
            total += quantity;

            invoiceContent.append(String.format("%-15s %-3d %-10s\n",
                    itemName,
                    quantity,
                    Styler.currency(price * quantity)));
        }

        invoiceContent.append("-".repeat(30))
                .append("+\n")
                .append(String.format("%-15s %-3d %-10s\n",
                        "Total", total, Styler.currency(bill)));

        return invoiceContent.toString();
    }

    private void generateReceipt(Map<MenuItem, Integer> orders) {
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
            System.err.printf("Error writing to %s: %s\n", fileName, e.getMessage());
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
