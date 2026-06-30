package com.example.poc.shared.messaging.rabbitmq.domain.models.declare;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import com.example.poc.shared.common.domain.models.Time;
import com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias;
import com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection;

public interface QueueDeclarator {
  Connection connection();
  QueueAlias alias();
  Time retryDelay();
  void declare(RabbitAdmin admin);
  default Boolean hasAlias(QueueAlias alias) {
    return alias().equals(alias);
  }
}
