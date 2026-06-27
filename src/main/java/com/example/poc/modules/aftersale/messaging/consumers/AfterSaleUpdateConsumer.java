package com.example.poc.modules.aftersale.messaging.consumers;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.aftersale.services.UpdateAfterSaleService;
import com.example.poc.shared.messaging.domain.annotations.RabbitConnection;
import com.example.poc.shared.messaging.domain.models.Message;
import com.example.poc.shared.messaging.domain.models.RetryListener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.poc.shared.messaging.domain.models.Connection.TRACKING;
import static com.example.poc.shared.messaging.domain.queues.AfterSaleUpdateQueue.AFTER_SALE_UPDATE_ALIAS;

@Component
@AllArgsConstructor
@RabbitConnection(connection = TRACKING, queue = AFTER_SALE_UPDATE_ALIAS)
public class AfterSaleUpdateConsumer implements RetryListener<Event> {
  private final UpdateAfterSaleService service;
  
  @Override
  public void onMessage(Message<Event> message) {
    service.update(message);
  }
}
