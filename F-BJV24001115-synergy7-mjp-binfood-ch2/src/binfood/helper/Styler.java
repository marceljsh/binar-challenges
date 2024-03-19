package binfood.helper;

public class Styler {

    public static String currency(int price) {
        return String.format("%,d", price).replace(',', '.');
    }
}
