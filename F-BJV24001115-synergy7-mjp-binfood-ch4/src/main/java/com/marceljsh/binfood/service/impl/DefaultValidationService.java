package com.marceljsh.binfood.service.impl;

import com.marceljsh.binfood.service.spec.ValidationService;
import com.marceljsh.binfood.util.RegexConst;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;

@Component
public class DefaultValidationService implements ValidationService {

  private final Validator validator;

  private final Pattern UUID_PATTERN = Pattern.compile(RegexConst.UUID);

  @Autowired
  public DefaultValidationService(Validator validator) {
    this.validator = validator;
  }

  @Override
  public <T> void validate(T object) {
    Set<ConstraintViolation<T>> violations = validator.validate(object);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  @Override
  public boolean isValidUUID(String id) {
    return UUID_PATTERN.matcher(id).matches();
  }
}
