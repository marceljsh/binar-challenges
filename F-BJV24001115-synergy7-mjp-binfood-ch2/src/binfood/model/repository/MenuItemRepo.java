package binfood.model.repository;

import binfood.model.entity.MenuItem;

import java.util.*;

public class MenuItemRepo {

    private long nextId = 1L;

    private static final MenuItem NULL_MENU_ITEM = new MenuItem(0L, "", 0);

    private final Map<Long, MenuItem> menuItems;

    public MenuItemRepo() {
        menuItems = new TreeMap<>();
    }
    
    public void add(MenuItem menuItem) {
        menuItem.setId(nextId++);
        menuItems.put(menuItem.getId(), menuItem);
    }
    
    public void remove(MenuItem menuItem) {
        menuItems.remove(menuItem.getId());
    }
    
    public Collection<MenuItem> getAll() {
        return menuItems.values();
    }
    
    public MenuItem getById(Long id) {
        MenuItem menuItem =  menuItems.get(id);
        return menuItem == null ? NULL_MENU_ITEM : menuItem;
    }

    public MenuItem getByName(String name) {
        for (MenuItem menuItem : menuItems.values()) {
            if (menuItem.getName().equals(name)) {
                return menuItem;
            }
        }
        return NULL_MENU_ITEM;
    }
    
    public int size() {
        return menuItems.size();
    }
}
