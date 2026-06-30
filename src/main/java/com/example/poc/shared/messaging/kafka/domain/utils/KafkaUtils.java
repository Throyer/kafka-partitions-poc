package com.example.poc.shared.messaging.kafka.domain.utils;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

public class KafkaUtils {
  private KafkaUtils() { }

  public static <T> KafkaTemplate<String, T> createTemplate(ProducerFactory<String, T> factory) {
    return new KafkaTemplate<>(factory);
  }
}
