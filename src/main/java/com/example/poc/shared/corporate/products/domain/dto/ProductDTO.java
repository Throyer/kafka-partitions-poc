package com.example.poc.shared.corporate.products.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
  private Long sku;
  private String name;
  private String description;
  private String imageUrl;
}
