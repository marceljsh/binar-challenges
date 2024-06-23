package com.marceljsh.binarfud.exhandling;

public class PageNotFoundException extends RuntimeException {

  public PageNotFoundException(String message) {
    super(message);
  }

}
