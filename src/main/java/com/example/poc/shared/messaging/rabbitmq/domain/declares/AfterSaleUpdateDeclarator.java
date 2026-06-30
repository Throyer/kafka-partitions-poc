package com.example.poc.shared.messaging.rabbitmq.domain.declares;

import com.example.poc.shared.common.domain.models.Time;
import com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias;
import com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection;
import com.example.poc.shared.messaging.rabbitmq.domain.models.declare.QueueDeclarator;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.poc.shared.messaging.rabbitmq.domain.models.QueueAlias.TRACKING_UPDATE_AFTERSALE;
import static com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection.TRACKING;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

@Component
public class AfterSaleUpdateDeclarator implements QueueDeclarator {
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
  public Time retryDelay() {
    return Time.of(5, SECONDS);
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
      var queue = durable(QUEUE_PREFIX + "-" + index)
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
}
