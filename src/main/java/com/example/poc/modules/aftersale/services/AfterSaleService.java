package com.example.poc.modules.aftersale.services;

import com.example.poc.modules.aftersale.domain.models.AfterSale;
import com.example.poc.modules.aftersale.domain.models.AfterSaleWithTimeline;
import com.example.poc.modules.aftersale.repositories.AfterSaleRepository;
import com.example.poc.modules.timeline.services.ComposeTimelineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AfterSaleService {
  private final AfterSaleRepository repository;
  private final ComposeTimelineService timelineService;

  public Optional<AfterSale> find(String orderNumber) {
    return repository.findByOrderNumber(orderNumber);
  }
  
  public boolean exists(String orderNumber) {
    return repository.existsByOrderNumber(orderNumber);
  }
  
  public AfterSaleWithTimeline show(String orderNumber) {
    var afterSale = repository.findByOrderNumber(orderNumber)
      .orElseThrow(() -> new RuntimeException("aqui seria um status de 404. //TODO: ajustar depois"));
    
    var timeline = timelineService.compose(orderNumber);
    return new AfterSaleWithTimeline(afterSale, timeline);
  }
}
