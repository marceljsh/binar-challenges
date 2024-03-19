package binfood.model.entity;

import java.util.Objects;

public class MenuItem {

    private Long id;
    private String name;
    private int price;

    public MenuItem(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
//        return price == menuItem.price && Objects.equals(id, menuItem.id) && Objects.equals(name, menuItem.name);
        return Objects.equals(name, ((MenuItem) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
