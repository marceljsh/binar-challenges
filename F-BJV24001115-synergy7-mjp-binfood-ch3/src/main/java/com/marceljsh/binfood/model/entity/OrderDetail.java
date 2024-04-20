package com.marceljsh.binfood.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

  private UUID id;

  private Order order;

  private Product product;

  private int quantity;

  private Long total;
}
