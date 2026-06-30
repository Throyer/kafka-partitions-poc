package com.example.poc.shared.messaging.kafka.services;

import static java.lang.String.format;

import com.example.poc.shared.messaging.kafka.domain.models.TopicAlias;
import com.example.poc.shared.messaging.kafka.domain.models.publisher.TopicProducer;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TopicPublisherManager {
  private final List<TopicProducer<?>> publishers;

  @SuppressWarnings("unchecked")
  public <T> TopicProducer<T> getByAlias(TopicAlias alias) {
    return (TopicProducer<T>) publishers
      .stream()
      .filter(publisher -> publisher.hasAlias(alias))
      .findFirst()
      .orElseThrow(() -> new RuntimeException(format("não foi possível encontrar tópico com alias: %s", alias)));
  }
}
