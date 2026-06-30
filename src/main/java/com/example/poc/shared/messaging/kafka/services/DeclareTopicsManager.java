package com.example.poc.shared.messaging.kafka.services;

import com.example.poc.shared.messaging.kafka.domain.models.declare.TopicDeclarator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DeclareTopicsManager {
  private final List<TopicDeclarator> topics;
  private final KafkaAdmin admin;

  public void declareAllTopics() {
    if (topics.isEmpty()) {
      log.warn("nenhum topico para declarar.");
      return;
    }

    for (var topic : topics) {
      try {
        log.debug("declarando topico: {}", topic.alias());
        topic.declare(admin);
      } catch (Exception exception) {
        log.error(
          "não foi possível declarar {}, {}",
          topic.alias(),
          exception.getMessage()
        );
      }
    }
  }
}
