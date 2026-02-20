package com.github.throyer.kafka.producer.services;

import java.util.Optional;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.throyer.kafka.producer.configuration.KafkaSettings;
import com.github.throyer.kafka.producer.models.Event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AfterSaleUpdateProducer {
  private final KafkaSettings settings;
  private final KafkaTemplate<String, Event> kafka;  

  public void publish(Event event) {
    var key = event.getOrderNumber();
    var future = kafka.send(settings.getTopic(), key, event);
    
    future.whenComplete((result, exception) -> Optional.ofNullable(exception)
      .ifPresent(error -> 
        log.error("falha ao publicar mensagem. key: {}, error: {}", key, error.getMessage())));
  }
}
