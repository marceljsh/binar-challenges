package com.marceljsh.binfood.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  private UUID id;

  private LocalDateTime orderTime;

  private String destination;

  private User customer;

  private boolean completed;
}
