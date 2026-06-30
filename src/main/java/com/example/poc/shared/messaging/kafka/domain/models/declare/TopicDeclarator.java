package com.example.poc.shared.messaging.kafka.domain.models.declare;

import org.springframework.kafka.core.KafkaAdmin;
import com.example.poc.shared.messaging.kafka.domain.models.TopicAlias;

public interface TopicDeclarator {
  TopicAlias alias();

  void declare(KafkaAdmin admin);

  default Boolean hasAlias(TopicAlias alias) {
    return alias().equals(alias);
  }
}
