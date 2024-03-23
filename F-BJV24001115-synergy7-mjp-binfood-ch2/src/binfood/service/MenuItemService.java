package binfood.service;

import binfood.model.entity.MenuItem;
import binfood.model.repository.MenuItemRepo;

public class MenuItemService {

    private final MenuItemRepo menuItemRepo;

    public MenuItemService() {
        menuItemRepo = new MenuItemRepo();
    }

    public MenuItemService(MenuItemRepo menuItemRepo) {
        this.menuItemRepo = menuItemRepo;
    }

    public void addMenuItem(String name, int price) {
        menuItemRepo.add(new MenuItem(0L, name, price));
    }

    public void removeMenuItem(String name) {
        MenuItem menuItem = menuItemRepo.getByName(name);
        menuItemRepo.remove(menuItem);
    }

    public MenuItem findByName(String name) {
        return menuItemRepo.getByName(name);
    }

    public MenuItem findById(Long id) {
        return menuItemRepo.getById(id);
    }

    public MenuItem[] getAll() {
        return menuItemRepo.getAll().toArray(new MenuItem[0]);
    }

    public int size() {
        return menuItemRepo.size();
    }
}
