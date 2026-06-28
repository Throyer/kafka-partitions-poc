package com.example.poc.modules.aftersale.domain.models.rupture;

import com.example.poc.modules.aftersale.domain.models.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rupture {
  private RuptureType type;
  private Item item;
}
