package com.example.poc.shared.messaging.domain.queues;

import static com.example.poc.shared.messaging.domain.models.Connection.TRACKING;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;
import java.util.Map;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.example.poc.shared.common.domain.models.Time;
import com.example.poc.shared.messaging.domain.models.Connection;
import com.example.poc.shared.messaging.domain.models.Queue;

@Slf4j
@Component
public class AfterSaleUpdateQueue implements Queue {
  public static final int PARTITION_COUNT = 8;
  
  public static final String AFTER_SALE_UPDATE_ALIAS = "after-sale-update";
  public static final String EXCHANGE_NAME = AFTER_SALE_UPDATE_ALIAS;
  public static final String HASH_HEADER = "order-number";
  public static final String QUEUE_PREFIX = AFTER_SALE_UPDATE_ALIAS;
  
  private final RabbitTemplate template;

  public AfterSaleUpdateQueue(@Qualifier("tracking-template") RabbitTemplate template) {
    this.template = template;
  }

  @Override
  public Connection connection() {
    return TRACKING;
  }

  @Override
  public String alias() {
    return AFTER_SALE_UPDATE_ALIAS;
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
        .withArgument("x-dead-letter-routing-key", "retry")
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
  public void setQueue(SimpleMessageListenerContainer container) {
    var queues = IntStream
      .range(0, PARTITION_COUNT)
      .mapToObj(AfterSaleUpdateQueue::queueName)
      .toArray(String[]::new);    
    container.setQueueNames(queues);
  }

  @Override
  public <T> void publish(T content, Map<String, String> headers) {
    try {
      template.convertAndSend(EXCHANGE_NAME, "", content, (post) -> {
        post.getMessageProperties().getHeaders().putAll(headers);
        return post;
      });
    } catch (Exception exception) {
      log.error(
        "erro ao publicar mensagem. queue: {} method: {}, error: {}",
        AFTER_SALE_UPDATE_ALIAS,
        "publish",
        exception.getMessage()
      );
    }
  }
}
