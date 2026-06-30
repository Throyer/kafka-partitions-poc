package com.example.poc.modules.updaters.services;

import org.springframework.stereotype.Service;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.aftersale.services.AfterSaleService;
import com.example.poc.modules.aftersale.services.CreateAfterSaleCompleteService;
import com.example.poc.modules.timeline.services.CreateReceivedEventService;
import com.example.poc.shared.messaging.rabbitmq.domain.models.message.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateAfterSaleService {
  private final AfterSaleService afterSaleService;
  private final CreateAfterSaleCompleteService createAfterSaleService;
  private final CreateReceivedEventService createReceivedEventService;
  private final UpdatersManager manager;

  public void update(Message<Event> message) {
    var queue = message.consumerQueue();
    var event = message.getBody();
    var orderNumber = event.getOrderNumber();

    log.info(
      "event received. queue: {}, orderNumber: {}, code: {}",
      queue,
      orderNumber,
      event.getStatusCode()
    );
    
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
