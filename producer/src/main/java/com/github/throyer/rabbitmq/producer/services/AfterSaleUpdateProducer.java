package com.github.throyer.rabbitmq.producer.services;

import com.github.throyer.rabbitmq.producer.models.Event;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.github.throyer.rabbitmq.producer.configuration.DeclareQueues.EXCHANGE_NAME;
import static com.github.throyer.rabbitmq.producer.configuration.DeclareQueues.partition;

@Slf4j
@Service
@AllArgsConstructor
public class AfterSaleUpdateProducer {
  private final RabbitTemplate rabbitmq;
  
  public void publish(Event event) {    
    try {
      rabbitmq.convertAndSend(EXCHANGE_NAME, partition(event), event);
    } catch (Exception exception) {
      
      log.error("falha ao publicar mensagem. error: {}", exception.getMessage());
    }
  }
}
