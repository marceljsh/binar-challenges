package com.marceljsh.binarfud.util;

import java.util.Map;

public class Constants {

  public static final class Msg {
    public static final String MERCHANT_NOT_FOUND = "merchant not found";
    public static final String ORDER_NOT_FOUND = "order not found";
    public static final String ORDER_DETAIL_NOT_FOUND = "order detail not found";
    public static final String PRODUCT_NOT_FOUND = "product not found";
    public static final String USER_NOT_FOUND = "user not found";

    private Msg() {
    }
  }

  public static final class Rgx {
    public static final String USERNAME = "^[a-z0-9._]*$";
    public static final String DISPLAY_NAME = "^[a-zA-Z0-9 ]*$";
    public static final String PASSWORD = "^[a-zA-Z0-9!@#$%^&*()_\\-+=\\[\\]{}|;:'\",.<>/?\\\\]*$";

    private Rgx() {
    }
  }

  public static final Map<String, String> OK_RESPONSE = Map.of("status", "ok");

  private Constants() {
  }
}
