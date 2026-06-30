package com.example.poc.modules.aftersale.domain.models;

import static org.springframework.beans.BeanUtils.copyProperties;
import java.util.List;
import com.example.poc.modules.aftersale.domain.models.rupture.Rupture;
import com.example.poc.modules.timeline.domain.models.Timeline;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AfterSaleWithTimeline {
  private String id;
  private String orderNumber;
  private Customer customer;
  private Payment payment;
  private Timeline timeline;  
  private List<Rupture> rupture;
  private List<Item> items;

  public AfterSaleWithTimeline(AfterSale afterSale, Timeline timeline) {
    copyProperties(afterSale, this);
    this.timeline = timeline;
  }
}
