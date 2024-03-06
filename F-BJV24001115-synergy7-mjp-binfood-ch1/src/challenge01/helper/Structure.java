/*
 * Copyright (c) 2024 Marcel Joshua
 * (https://github.com/marceljsh)
 * 
 * Developed under SYNRGY Academy Batch 7
 */

package challenge01.helper;

public class Structure {
	
	public static String currency(int number) {
		return String.format("%,d", number).replace(',', '.');
	}
}
