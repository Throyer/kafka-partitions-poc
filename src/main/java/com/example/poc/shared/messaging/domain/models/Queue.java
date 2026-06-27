package com.example.poc.shared.messaging.domain.models;

import java.util.Map;
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
  
  void setQueue(SimpleMessageListenerContainer container);
  
  default <T> void publish(T content) { }

  default <T> void publish(T content, Map<String, String> headers) { }

  default Boolean hasAlias(String alias) {
    return alias().equalsIgnoreCase(alias);
  }
}
