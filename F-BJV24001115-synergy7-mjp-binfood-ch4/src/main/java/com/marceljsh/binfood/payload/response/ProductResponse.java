package com.marceljsh.binfood.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductResponse {

  private UUID id;

  private String name;

  private long price;

  private String seller;
}
