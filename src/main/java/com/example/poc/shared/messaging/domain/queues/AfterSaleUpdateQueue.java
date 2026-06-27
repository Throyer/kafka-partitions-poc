package com.example.poc.shared.messaging.domain.queues;

import static com.example.poc.shared.messaging.domain.models.Connection.TRACKING;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;
import com.example.poc.shared.common.domain.models.Time;
import com.example.poc.shared.messaging.domain.models.Connection;
import com.example.poc.shared.messaging.domain.models.Queue;

@Component
public class AfterSaleUpdateQueue implements Queue {
  public static final int PARTITION_COUNT = 8;
  
  public static final String ALIAS = "after-sale-update";
  public static final String EXCHANGE_NAME = ALIAS;
  public static final String HASH_HEADER = "order-number";
  public static final String QUEUE_PREFIX = ALIAS;
    
  @Override
  public Connection connection() {
    return TRACKING;
  }

  @Override
  public String alias() {
    return ALIAS;
  }

  @Override
  public Time retryDelay() {
    return Time.of(5, SECONDS);
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
  public void declare(RabbitAdmin admin) {
    var retryExchange = directExchange(EXCHANGE_NAME + "-retry")
      .build();
    
    var exchange = new CustomExchange(
      EXCHANGE_NAME,
      "x-consistent-hash",
      true,
      false,
      Map.of("hash-header", HASH_HEADER)
    );

    admin.declareExchange(exchange);
    admin.declareExchange(retryExchange);
    
    var retryQueue = durable(EXCHANGE_NAME + "-retry")
      .withArgument("x-dead-letter-exchange", EXCHANGE_NAME)
      .withArgument("x-message-ttl", retryDelay().toMillis())
      .build();
    
    admin.declareQueue(retryQueue);
    
    admin.declareBinding(bind(retryQueue)
      .to(retryExchange)
      .with("retry")
      .noargs());
    
    for (int index = 0; index < PARTITION_COUNT; index++) {
      var queue = durable(QUEUE_PREFIX + index)
        .withArgument("x-single-active-consumer", true)
        .withArgument("x-dead-letter-exchange", EXCHANGE_NAME + "-retry")
        .withArgument("x-dead-letter-routing-key", EXCHANGE_NAME + "retry")
        .build();

      var binding = bind(queue)
        .to(exchange)
        .with("1")
        .noargs();

      admin.declareQueue(queue);
      admin.declareBinding(binding);
    }
  }

  private static String queueName(int index) {
    return QUEUE_PREFIX + index;
  }

  @Override
  public SimpleMessageListenerContainer setQueueOnContainer(SimpleMessageListenerContainer container) {
    var queues = IntStream
      .range(0, PARTITION_COUNT)
      .mapToObj(AfterSaleUpdateQueue::queueName)
      .toArray(String[]::new);    
    container.setQueueNames(queues);
    return container;
  }
}
