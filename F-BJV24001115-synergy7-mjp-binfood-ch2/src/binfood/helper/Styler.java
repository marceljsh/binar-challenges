package binfood.helper;

public class Styler {

    private Styler() {}

    public static String currency(int price) {
        return String.format("%,d", price).replace(',', '.');
    }
}
