package com.example.poc.shared.messaging.rabbitmq.domain.models.settings;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias;
import com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection;

public interface QueueSettings {
  Connection connection();
  QueueAlias alias();
  int maxRetryAttempts();
  boolean enabled();
  
  void setQueue(SimpleMessageListenerContainer container);
  
  default Boolean hasAlias(QueueAlias alias) {
    return alias().equals(alias);
  }
}
