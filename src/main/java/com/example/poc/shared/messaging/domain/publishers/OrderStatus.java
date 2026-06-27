package com.example.poc.shared.messaging.domain.publishers;

import com.example.poc.shared.messaging.domain.models.QueueAlias;
import com.example.poc.shared.messaging.domain.models.publisher.QueueProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.example.poc.shared.messaging.domain.models.QueueAlias.OMS_ORDER_STATUS;

@Component
public class OrderStatus extends QueueProducer {
  public OrderStatus(
    @Qualifier("oms-template") RabbitTemplate template
  ) {
    super(template);
  }

  @Override
  public QueueAlias alias() {
    return OMS_ORDER_STATUS;
  }

  @Override
  public String exchange() {
    return "order.after-sale";
  }

  @Override
  public String routingKey() {
    return "order-status";
  }
}
