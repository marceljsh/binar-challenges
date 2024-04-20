package com.marceljsh.binfood.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class Verifier implements AutoCloseable {

  private static Verifier instance;

  private final ValidatorFactory validatorFactory;

  private final Validator validator;

  private Verifier() {
    this.validatorFactory = Validation.buildDefaultValidatorFactory();
    this.validator = validatorFactory.getValidator();
  }

  public static Verifier getInstance() {
    if (instance == null) {
      instance = new Verifier();
    }
    return instance;
  }

  @Override
  public void close() {
    validatorFactory.close();
  }

  public boolean isValidUUID(String uuid) {
    final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    return uuid != null && uuid.matches(UUID_REGEX);
  }

  public <T> Set<ConstraintViolation<T>> verify(T request) {
    return validator.validate(request);
  }
}
