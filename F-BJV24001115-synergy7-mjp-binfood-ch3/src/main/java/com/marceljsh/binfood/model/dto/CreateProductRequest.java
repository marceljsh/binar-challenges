package com.marceljsh.binfood.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateProductRequest {

  @NotBlank
  private String name;

  @NotNull
  @Positive
  private Long price;

  @NotBlank
  private String merchantId;
}
