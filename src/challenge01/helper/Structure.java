package challenge01.helper;

public class Format {
	
	public static String currency(int number) {
		return String.format("%,d", number).replace(',', '.');
	}
}
