package com.marceljsh.binfood.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateMerchantRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String location;
}
