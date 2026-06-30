package com.example.poc.shared.messaging.kafka.domain.models.publisher;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@AllArgsConstructor
public abstract class TopicProducer<T> implements TopicPublishSource {
  private final KafkaTemplate<String, T> template;

  public void publish(T message) {
    try {
      template.send(topic(), message);
    } catch (Exception exception) {
      log.error(
        "erro ao publicar mensagem. tópico: {} method: {}, error: {}",
        alias(),
        "publish",
        exception.getMessage()
      );
    }
  }

  public void publish(T message, String key) {
    try {
      template.send(topic(), key, message);
    } catch (Exception exception) {
      log.error(
        "erro ao publicar mensagem. tópico: {} method: {}, error: {}",
        alias(),
        "publish",
        exception.getMessage()
      );
    }
  }
}
