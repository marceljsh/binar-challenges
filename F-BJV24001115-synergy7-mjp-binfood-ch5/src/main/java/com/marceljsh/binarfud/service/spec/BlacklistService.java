package com.marceljsh.binarfud.service.spec;

public interface BlacklistService {

  void addToBlacklist(String token);

  boolean isBlacklisted(String token);
}
