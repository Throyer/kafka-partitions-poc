package com.example.poc.shared.messaging.domain.publishers;

import com.example.poc.shared.messaging.domain.models.QueueAlias;
import com.example.poc.shared.messaging.domain.models.publisher.QueueProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.example.poc.shared.messaging.domain.models.QueueAlias.TRACKING_UPDATE_AFTERSALE;

@Slf4j
@Component
public class UpdateAfterSale extends QueueProducer {
  public UpdateAfterSale(
    @Qualifier("tracking-template") RabbitTemplate template
  ) {
    super(template);
  }
  
  @Override
  public QueueAlias alias() {
    return TRACKING_UPDATE_AFTERSALE;
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
