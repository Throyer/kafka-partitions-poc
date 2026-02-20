package com.github.throyer.kafka.consumer.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.throyer.kafka.consumer.models.Event;
import com.github.throyer.kafka.consumer.services.ProcessAfterSaleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EventListener {
  private final ProcessAfterSaleService service;
  
  @KafkaListener(
    topics = "order-events",
    groupId = "after-sale-processor",
    containerFactory = "kafka-listener-container"
  )
  public void processar(@Payload Event event) {
    service.process(event);
  }
}
