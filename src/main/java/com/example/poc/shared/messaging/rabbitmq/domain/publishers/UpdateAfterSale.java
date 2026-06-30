package com.example.poc.shared.messaging.rabbitmq.domain.publishers;

import com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias;
import com.example.poc.shared.messaging.rabbitmq.domain.models.publisher.QueueProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
// @Component
public class UpdateAfterSale extends QueueProducer {
  public UpdateAfterSale(
    @Qualifier("tracking-template") RabbitTemplate template
  ) {
    super(template);
  }
  
  @Override
  public QueueAlias alias() {
    return null;
  }

  @Override
  public String exchange() {
    return "after-sale-update";
  }

  @Override
  public String routingKey() {
    return "";
  }
}
