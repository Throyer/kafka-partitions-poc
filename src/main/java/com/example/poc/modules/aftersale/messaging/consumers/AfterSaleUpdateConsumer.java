package com.example.poc.modules.aftersale.messaging.consumers;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.aftersale.services.UpdateAfterSaleServiceAndRegisterEventService;
import com.example.poc.shared.messaging.domain.annotations.RabbitConnection;
import com.example.poc.shared.messaging.domain.models.message.Message;
import com.example.poc.shared.messaging.domain.models.message.RetryListener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.poc.shared.messaging.domain.models.connection.Connection.TRACKING;
import static com.example.poc.shared.messaging.domain.models.QueueAlias.TRACKING_UPDATE_AFTERSALE;

@Component
@AllArgsConstructor
@RabbitConnection(connection = TRACKING, queue = TRACKING_UPDATE_AFTERSALE)
public class AfterSaleUpdateConsumer implements RetryListener<Event> {
  private final UpdateAfterSaleServiceAndRegisterEventService service;
  
  @Override
  public void onMessage(Message<Event> message) {
    service.update(message);
  }
}
