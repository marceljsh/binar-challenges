package com.marceljsh.binarfud.service;

import com.marceljsh.binarfud.service.spec.BlacklistService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryBlacklistService implements BlacklistService {

  private final Set<String> blacklist = new HashSet<>();

  @Override
  public void addToBlacklist(String token) {
    blacklist.add(token);
  }

  @Override
  public boolean isBlacklisted(String token) {
    return blacklist.contains(token);
  }
}
