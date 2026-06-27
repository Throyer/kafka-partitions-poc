package com.example.poc.shared.messaging.domain.models;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import com.example.poc.shared.common.domain.models.Time;

public interface Queue {
  Connection connection();
  String alias();
  Time retryDelay();
  int maxRetryAttempts();
  
  boolean enabled();
  
  void declare(RabbitAdmin admin);
  
  SimpleMessageListenerContainer setQueueOnContainer(SimpleMessageListenerContainer container);

  default Boolean hasAlias(String alias) {
    return alias().equalsIgnoreCase(alias);
  }
}
