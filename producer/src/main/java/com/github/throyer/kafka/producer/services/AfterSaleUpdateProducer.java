package com.github.throyer.kafka.producer.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.throyer.kafka.producer.models.Event;
import com.github.throyer.kafka.producer.utils.JSON;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AfterSaleUpdateProducer {
  private final KafkaTemplate<String, Event> kafkaTemplate;  

  public void publish(Event event) {    
    try {
      // Use orderNumber as key to ensure same order goes to same partition
      var key = event.getOrderNumber();
      
      var future = kafkaTemplate.send("order-events", key, event);
      
      future.whenComplete((result, exception) -> {
        if (exception == null) {
          var partition = result.getRecordMetadata().partition();
          var offset = result.getRecordMetadata().offset();

          log.debug("publish. topic: {}, partition: {}, offset: {}, event: {}", "order-events", partition, offset, JSON.stringify(event));
        } else {
          log.error("falha ao publicar mensagem. key: {}, error: {}", key, exception.getMessage());
        }
      });
      
    } catch (Exception exception) {
      log.error("falha ao publicar mensagem. error: {}", exception.getMessage());
    }
  }
}
