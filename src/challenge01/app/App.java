/*
 * Copyright (c) 2024 Marcel Joshua
 * (https://github.com/marceljsh)
 * 
 * Developed under SYNRGY Academy Batch 7
 */

package challenge01.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import challenge01.helper.Structure;
import challenge01.model.entity.MenuItem;
import challenge01.model.repository.MenuItemRepo;

public class App {

	private static MenuItemRepo menuItemRepo = new MenuItemRepo();

	public void run() {
		init();

		Scanner scanner = new Scanner(System.in);
		Map<MenuItem, Integer> orders = new HashMap<>();

		int choice;
		do {
			showMenu();
			System.out.print("=> ");
			choice = scanner.nextInt();
			if (choice == 99 || choice == 0) {
				break;
			}

			MenuItem menuItem = menuItemRepo.getMenuItem(choice);

		} while (choice != 0);

		scanner.close();
	}

	public static void init() {
		menuItemRepo.addMenuItem("Nasi Goreng", 15000);
		menuItemRepo.addMenuItem("Mie Goreng", 13000);
		menuItemRepo.addMenuItem("Nasi + Ayam", 18000);
		menuItemRepo.addMenuItem("Es Teh Manis", 3000);
		menuItemRepo.addMenuItem("Es Jeruk", 5000);
	}

	public static void showMenu() {
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
}
