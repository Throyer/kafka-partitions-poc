package com.example.poc.shared.messaging.kafka.domain.models.publisher;

import com.example.poc.shared.messaging.kafka.domain.models.TopicAlias;

public interface TopicPublishSource {
  TopicAlias alias();

  String topic();

  default boolean hasAlias(TopicAlias alias) {
    return alias().equals(alias);
  }
}
