package com.example.poc.shared.messaging.rabbitmq.domain.queues;

import static com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias.TRACKING_UPDATE_AFTERSALE;
import static com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection.TRACKING;
import java.util.stream.IntStream;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;
import com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias;
import com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection;
import com.example.poc.shared.messaging.rabbitmq.domain.models.settings.QueueSettings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AfterSaleUpdateQueue implements QueueSettings {
  public static final int PARTITION_COUNT = 8;
  
  public static final String EXCHANGE_NAME = "after-sale-update";
  public static final String HASH_HEADER = "order-number";
  public static final String QUEUE_PREFIX = "after-sale-update";
  
  @Override
  public Connection connection() {
    return TRACKING;
  }

  @Override
  public QueueAlias alias() {
    return TRACKING_UPDATE_AFTERSALE;
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
    var queues = IntStream
      .range(0, PARTITION_COUNT)
      .mapToObj(index -> QUEUE_PREFIX + "-" + index)
      .toArray(String[]::new);    
    container.setQueueNames(queues);
  }
}
