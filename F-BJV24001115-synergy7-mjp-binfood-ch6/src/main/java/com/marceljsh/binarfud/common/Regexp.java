package com.marceljsh.binarfud.common;

public class Regexp {

  private Regexp() {
  }

  public static final String USERNAME = "^[a-z0-9._]*$";

  public static final String DISPLAY_NAME = "^[a-zA-Z0-9 ]*$";

  public static final String RAW_PASSWORD = "^[a-zA-Z0-9!@#$%^&*()_\\-+=\\[\\]{}|;:'\",.<>/?\\\\]*$";
}
