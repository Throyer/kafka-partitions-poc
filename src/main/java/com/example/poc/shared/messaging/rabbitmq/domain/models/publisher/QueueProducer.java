package com.example.poc.shared.messaging.rabbitmq.domain.models.publisher;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public abstract class QueueProducer implements QueuePublishSource {
  private final RabbitTemplate template;

  public <T> void publish(T message) {
    try {
      template.convertAndSend(exchange(), routingKey(), message);
    } catch (Exception exception) {
      log.error(
        "erro ao publicar mensagem. queue: {} method: {}, error: {}",
        alias(),
        "publish",
        exception.getMessage()
      );
    }
  }

  public <T> void publish(T message, Map<String, String> headers) {
    try {
      template.convertAndSend(exchange(), routingKey(), message, (post) -> {
        post.getMessageProperties().getHeaders().putAll(headers);
        return post;
      });
    } catch (Exception exception) {
      log.error(
        "erro ao publicar mensagem. queue: {} method: {}, error: {}",
        alias(),
        "publish",
        exception.getMessage()
      );
    }
  }
}
