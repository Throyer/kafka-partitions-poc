package com.example.poc.modules.triggers.messaging.producers;

import com.example.poc.modules.triggers.domain.dtos.OrderDTO;
import com.example.poc.shared.messaging.services.QueueManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.poc.shared.messaging.domain.queues.OrderStatusQueue.ORDER_STATUS_ALIAS;

@Service
@AllArgsConstructor
public class OrderStatusProducer {
  private final QueueManager manager;

  public void publish(OrderDTO order) {
    manager.getByAlias(ORDER_STATUS_ALIAS)
      .ifPresent(queue -> queue.publish(order));
  }
}
