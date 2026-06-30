package com.example.poc.modules.aftersale.messaging.consumers;

import static com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias.TRACKING_UPDATE_AFTERSALE;
import static com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection.TRACKING;
import org.springframework.stereotype.Component;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.updaters.services.UpdateAfterSaleService;
import com.example.poc.shared.messaging.rabbitmq.domain.annotations.RabbitConnection;
import com.example.poc.shared.messaging.rabbitmq.domain.models.message.Message;
import com.example.poc.shared.messaging.rabbitmq.domain.models.message.RetryListener;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@RabbitConnection(connection = TRACKING, queue = TRACKING_UPDATE_AFTERSALE)
public class AfterSaleUpdateConsumer implements RetryListener<Event> {
  private final UpdateAfterSaleService service;
  
  @Override
  public void onMessage(Message<Event> message) {
    service.update(message);
  }
}
