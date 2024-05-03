package com.marceljsh.binfood.util;

public class RegexConst {

  private RegexConst() {
  }

  public static final String USERNAME = "^[a-z0-9._]*$";

  public static final String DISPLAY_NAME = "^[\\p{L}\\p{N}\\p{P}\\p{Z}\\p{M}]*$";

  public static final String PASSWORD = "^[a-zA-Z0-9!@#$%^&*()_\\-+=\\[\\]{}|;:'\",.<>/?\\\\]*$";

  public static final String UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
}
