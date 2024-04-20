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
public class Merchant {

  private UUID id;

  private String name;

  private String location;

  private Boolean open;
}
