package com.marceljsh.binfood.payload.request;

import com.marceljsh.binfood.util.RegexConst;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailAddRequest {

  @Min(value = 1, message = "quantity must be greater than or equal to {value}")
  private int quantity;

  @Min(value = 1, message = "total_price must be greater than or equal to {value}")
  private long totalPrice;

  @NotBlank(message = "order_id cannot be empty")
  @Pattern(regexp = RegexConst.UUID, message = "order_id must be 36 characters")
  @Size(min = 36, max = 36, message = "order_id must be {min} characters")
  private String orderId;

  @NotBlank(message = "product_id cannot be empty")
  @Pattern(regexp = RegexConst.UUID, message = "product_id must be 36 characters")
  @Size(min = 36, max = 36, message = "product_id must be {min} characters")
  private String productId;
}
