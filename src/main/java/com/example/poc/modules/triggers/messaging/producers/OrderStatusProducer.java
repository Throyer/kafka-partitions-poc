package com.example.poc.modules.triggers.messaging.producers;

import static com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias.OMS_ORDER_STATUS;
import org.springframework.stereotype.Service;
import com.example.poc.shared.corporate.orders.domain.dto.OrderDTO;
import com.example.poc.shared.messaging.rabbitmq.services.QueuePublisherManager;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderStatusProducer {
  private final QueuePublisherManager manager;

  public void publish(OrderDTO order) {
    manager.getByAlias(OMS_ORDER_STATUS)
      .publish(order);
  }
}
