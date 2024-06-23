package com.marceljsh.binarfud.merchant;

import com.marceljsh.binarfud.common.Regexp;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MerchantAddRequest {

  @Size(
    min = 3,
    max = 80,
    message = "Merchant name must be {min}-{max} characters long"
  )
  @Pattern(
    regexp = Regexp.DISPLAY_NAME,
    message = "Merchant name can only have alphanumeric characters and spaces"
  )
  private String name;

  @Size(
    min = 3,
    max = 255,
    message = "Merchant location must be {min}-{max} characters long"
  )
  private String location;

}
