package com.example.poc.modules.aftersale.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "after_sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AfterSale {
  @Id
  private String id;

  @Indexed
  private String orderNumber;
}
