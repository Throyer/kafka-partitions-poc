package com.example.poc.modules.triggers.messaging.consumers;

import static com.example.poc.shared.messaging.domain.models.connection.Connection.OMS;
import static com.example.poc.shared.messaging.domain.models.QueueAlias.OMS_ORDER_STATUS;

import com.example.poc.shared.corporate.orders.domain.dto.OrderDTO;
import org.springframework.stereotype.Component;
import com.example.poc.modules.triggers.services.ProcessExternalEventService;
import com.example.poc.shared.common.domain.utils.JSON;
import com.example.poc.shared.messaging.domain.annotations.RabbitConnection;

@Component
@RabbitConnection(connection = OMS, queue = OMS_ORDER_STATUS)
public class OrderStatusConsumer extends TriggerConsumer<OrderDTO> {
  public OrderStatusConsumer(ProcessExternalEventService service) {
    super(service);
  }

  @Override
  public OrderDTO parse(String message) {
    return JSON.parse(message, OrderDTO.class);
  }
}
