package com.example.poc.shared.messaging.rabbitmq.domain.models.publisher;

import com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias;

public interface QueuePublishSource {
  QueueAlias alias();
  String exchange();
  String routingKey();

  default boolean hasAlias(QueueAlias alias) {
    return alias().equals(alias);
  }
}