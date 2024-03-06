/*
 * Copyright (c) 2024 Marcel Joshua
 * (https://github.com/marceljsh)
 * 
 * Developed under SYNRGY Academy Batch 7
 */

package challenge01.model.repository;

import java.util.LinkedHashSet;

import challenge01.model.entity.MenuItem;

public class MenuItemRepo {

	private static Long nextId = 1L;

	private LinkedHashSet<MenuItem> menuItems;

	public MenuItemRepo() {
		menuItems = new LinkedHashSet<MenuItem>();
	}

	public int size() {
		return menuItems.size();
	}

	public LinkedHashSet<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void addMenuItem(String name, int price) {
		MenuItem menuItem = new MenuItem(nextId++, name, price);
		menuItems.add(menuItem);
	}

	public void removeMenuItem(String name) {
		MenuItem menuItem = getMenuItem(name);
		if (menuItem != null) {
			menuItems.remove(menuItem);
		}
	}

	public void removeMenuItem(int id) {
		MenuItem menuItem = getMenuItem(id);
		if (menuItem != null) {
			menuItems.remove(menuItem);
		}
	}

	public MenuItem getMenuItem(String name) {
		for (MenuItem menuItem : menuItems) {
			if (menuItem.getName().equals(name)) {
				return menuItem;
			}
		}
		return null;
	}

	public MenuItem getMenuItem(int id) {
		for (MenuItem menuItem : menuItems) {
			if (menuItem.getId() == id) {
				return menuItem;
			}
		}
		return null;
	}

	public int getLongestNameLength() {
		int longest = 0;

		if (menuItems.isEmpty()) {
			return longest;
		}

		for (MenuItem menuItem : menuItems) {
			String name = menuItem.getName();
			if (name.length() > longest) {
				longest = name.length();
			}
		}
		return longest;
	}
}
