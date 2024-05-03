package com.marceljsh.binfood.service.spec;

import jakarta.validation.ConstraintViolationException;

public interface ValidationService {
  <T> void validate(T object) throws ConstraintViolationException;
  boolean isValidUUID(String id);
}
