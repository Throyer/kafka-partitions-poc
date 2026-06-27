package com.example.poc.shared.messaging.domain.queues;

import static com.example.poc.shared.messaging.domain.models.QueueAlias.OMS_ORDER_STATUS;
import static com.example.poc.shared.messaging.domain.models.connection.Connection.OMS;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;
import com.example.poc.shared.messaging.domain.models.QueueAlias;
import com.example.poc.shared.messaging.domain.models.connection.Connection;
import com.example.poc.shared.messaging.domain.models.settings.QueueSettings;

@Component
public class OrderStatusQueue implements QueueSettings {
  @Override
  public Connection connection() {
    return OMS;
  }

  @Override
  public QueueAlias alias() {
    return OMS_ORDER_STATUS;
  }
  
  @Override
  public int maxRetryAttempts() {
    return 2;
  }

  @Override
  public boolean enabled() {
    return true;
  }
  
  @Override
  public void setQueue(SimpleMessageListenerContainer container) {
    container.setQueueNames("order.after-sale");
  }
}
