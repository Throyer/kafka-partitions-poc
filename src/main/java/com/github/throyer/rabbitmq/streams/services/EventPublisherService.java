package com.github.throyer.rabbitmq.streams.services;

import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.streams.models.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisherService {

  private final RabbitStreamTemplate rabbitStreamTemplate;

  public void publishEvent(Event event) {
    log.info("Publicando evento na stream: id={}, code={}", event.getId(), event.getCode());
    
    var future = rabbitStreamTemplate.convertAndSend(event);
    
    future.thenAccept(success -> {
      if (success) {
        log.debug("Evento publicado com sucesso: {}", event);
      } else {
        log.error("Falha ao publicar evento: {}", event);
      }
    }).exceptionally(ex -> {
      log.error("Erro ao publicar evento: {}", event, ex);
      return null;
    });
  }
}
