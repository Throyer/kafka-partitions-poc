package com.github.throyer.rabbitmq.consumer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static org.springframework.data.mongodb.core.index.IndexDirection.ASCENDING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("after_sale")
public class AfterSale {
  @Id
  private String id;
  
  @Indexed(name = "orderNumber_1", direction = ASCENDING)
  private String orderNumber;
  
  private List<AfterSaleEvent> events;
}
