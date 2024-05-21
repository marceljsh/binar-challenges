package com.marceljsh.binarfud.validation.validator;

import com.marceljsh.binarfud.validation.annotation.ValidUUID;
import jakarta.validation.ConstraintValidator;

import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

  @Override
  public void initialize(ValidUUID constraintAnnotation) {
  }

  @Override
  public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
    try {
      UUID.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
