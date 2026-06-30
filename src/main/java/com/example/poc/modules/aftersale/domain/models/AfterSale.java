package com.example.poc.modules.aftersale.domain.models;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.poc.modules.aftersale.domain.models.rupture.Rupture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "after_sale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AfterSale {
  @Id
  private String id;

  @Indexed
  private String orderNumber;
  
  private Customer customer;
  private Payment payment;
  private List<Rupture> rupture;
  private List<Item> items;
}
