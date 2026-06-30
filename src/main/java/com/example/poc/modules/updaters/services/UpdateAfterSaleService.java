package com.example.poc.modules.updaters.services;

import org.springframework.stereotype.Service;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.aftersale.services.AfterSaleService;
import com.example.poc.modules.aftersale.services.CreateAfterSaleCompleteService;
import com.example.poc.modules.timeline.services.CreateReceivedEventService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UpdateAfterSaleService {
  private final AfterSaleService afterSaleService;
  private final CreateAfterSaleCompleteService createAfterSaleService;
  private final CreateReceivedEventService createReceivedEventService;
  private final UpdatersManager manager;

  public void update(Event event) {
    var orderNumber = event.getOrderNumber();

    if (!event.is("1011")) {
      if (!afterSaleService.exists(orderNumber)) {
        createAfterSaleService.create(orderNumber);
        return;
      }
    }

    manager.update(event);
    createReceivedEventService.create(event);
  }
}
