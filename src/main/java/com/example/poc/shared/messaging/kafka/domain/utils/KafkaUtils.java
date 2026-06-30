package com.example.poc.shared.messaging.kafka.domain.utils;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

public class KafkaUtils {
  private KafkaUtils() { }

  public static KafkaTemplate<String, Object> createTemplate(ProducerFactory<String, Object> factory) {
    return new KafkaTemplate<>(factory);
  }
}
