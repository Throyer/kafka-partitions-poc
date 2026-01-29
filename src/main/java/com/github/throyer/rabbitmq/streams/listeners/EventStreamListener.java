package com.github.throyer.rabbitmq.streams.listeners;

import com.github.throyer.rabbitmq.streams.models.Event;
import com.github.throyer.rabbitmq.streams.utils.JSON;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventStreamListener {

  @RabbitListener(queues = "events-stream")
  public void receiveEvent(Event event) {
    log.info("Evento recebido: id={}, code={}", event.getId(), event.getCode());    
    log.debug("Processando evento: {}", JSON.stringify(event));
  }
}
