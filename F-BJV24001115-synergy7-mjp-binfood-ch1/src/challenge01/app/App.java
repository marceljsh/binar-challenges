/*
 * Copyright (c) 2024 Marcel Joshua
 * (https://github.com/marceljsh)
 * 
 * Developed under SYNRGY Academy Batch 7
 */

package challenge01.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import challenge01.helper.Structure;
import challenge01.model.entity.MenuItem;
import challenge01.model.repository.MenuItemRepo;

public class App {

	private static MenuItemRepo menuItemRepo = new MenuItemRepo();

	public void run() {
		seed();

		Scanner scanner = new Scanner(System.in);
		Map<MenuItem, Integer> orders = new LinkedHashMap<>();

		int choice;
		while (true) {
			mainMenu();
			System.out.print("=> ");
			choice = scanner.nextInt();
			System.out.println();

			if (choice == 0) {
				break;

			} else if (choice == 99) {
				confirmOrder(orders);
				System.err.print("=> ");
				int action = scanner.nextInt();

				if (action == 0) {
					break;
				} else if (action == 1) {
					receipt(orders);
					break;
				}

			} else if (choice > 0 && choice <= menuItemRepo.size()) {
				System.out.println("=".repeat(26));
				System.out.println("Berapa pesanan anda");
				System.out.println("=".repeat(26));

				MenuItem menuItem = menuItemRepo.getMenuItem(choice);
				System.out.printf("%-15s | %s\n", menuItem.getName(), Structure.currency(menuItem.getPrice()));
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

	public static void seed() {
		menuItemRepo.addMenuItem("Nasi Goreng", 15000);
		menuItemRepo.addMenuItem("Mie Goreng", 13000);
		menuItemRepo.addMenuItem("Nasi + Ayam", 18000);
		menuItemRepo.addMenuItem("Es Teh Manis", 3000);
		menuItemRepo.addMenuItem("Es Jeruk", 5000);
	}

	public static void mainMenu() {
		System.out.println("=".repeat(26));
		System.out.println("Selamat datang di Binarfud");
		System.out.println("=".repeat(26) + "\n");
		System.out.println("Silahkan pilih makanan :");

		int i = 1;
		for (MenuItem menuItem : menuItemRepo.getMenuItems()) {
			System.out.printf("%d. %-15s | %s\n", i, menuItem.getName(), Structure.currency(menuItem.getPrice()));
			i++;
		}

		System.out.println("99. Pesan dan Bayar");
		System.out.println("0. Keluar aplikasi");
	}

	public static String invoice(Map<MenuItem, Integer> orders) {
		StringBuilder invoiceContent = new StringBuilder();
		int total = 0;
		int bill = 0;

		for (MenuItem menuItem : orders.keySet()) {
			int quantity = orders.get(menuItem);
			bill += menuItem.getPrice() * quantity;
			total += quantity;
			invoiceContent.append(
					String.format(
							"%-15s %-3d %-10s\n",
							menuItem.getName(),
							quantity,
							Structure.currency(menuItem.getPrice() * quantity)));
		}

		invoiceContent
				.append("-".repeat(31))
				.append("+")
				.append("\n")
				.append(String.format(
						"%-15s %-3d %-10s\n",
						"Total",
						total,
						Structure.currency(bill)));

		return invoiceContent.toString();
	}

	public static void confirmOrder(Map<MenuItem, Integer> orders) {
		System.out.println("=".repeat(26));
		System.out.println("Konfirmasi & Pembayaran");
		System.out.println("=".repeat(26) + "\n");

		System.out.println(invoice(orders));

		System.out.println("\n1. Konfirmasi dan Bayar");
		System.out.println("2. Kembali ke menu utama");
		System.out.println("0. Keluar aplikasi\n");
	}

	public static void receipt(Map<MenuItem, Integer> orders) {
		LocalDate date = LocalDate.now();
		String fileName = String.format("receipt/receipt_%s.txt", date);

		new File("receipt").mkdir();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
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

		} catch (IOException e) {
			System.err.printf("error writing to %s: %s", fileName, e.getMessage());
		}
	}
}
