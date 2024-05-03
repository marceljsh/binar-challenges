package com.marceljsh.binfood.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

  private TokenGenerator() {
  }

  private static final int BYTE_LENGTH = 128;

  private static final SecureRandom secureRandom = new SecureRandom();

  private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

  public static String generateRandomToken() {
    byte[] token = new byte[BYTE_LENGTH];
    secureRandom.nextBytes(token);
    return base64Encoder.encodeToString(token);
  }
}
