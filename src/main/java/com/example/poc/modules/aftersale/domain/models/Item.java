package com.example.poc.modules.aftersale.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
  private String sku;
  private String name;
  private String imageUrl;
  private String description;
}
