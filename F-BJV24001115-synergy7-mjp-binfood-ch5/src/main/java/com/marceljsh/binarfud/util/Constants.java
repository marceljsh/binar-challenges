package com.marceljsh.binarfud.util;

public class Constants {

  private Constants() {}
  
  public static final String MSG_MERCHANT_NOT_FOUND = "merchant not found";

  public static final String REGEX_USERNAME = "^[a-z0-9._]*$";
  public static final String REGEX_DISPLAY_NAME = "^[\\p{L}\\p{N}\\p{P}\\p{Z}\\p{M}]*$";
  public static final String REGEX_PASSWORD = "^[a-zA-Z0-9!@#$%^&*()_\\-+=\\[\\]{}|;:'\",.<>/?\\\\]*$";
  public static final String REGEX_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
}
