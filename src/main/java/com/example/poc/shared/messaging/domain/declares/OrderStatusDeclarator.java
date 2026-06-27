package com.example.poc.shared.messaging.domain.declares;

import com.example.poc.shared.common.domain.models.Time;
import com.example.poc.shared.messaging.domain.models.QueueAlias;
import com.example.poc.shared.messaging.domain.models.connection.Connection;
import com.example.poc.shared.messaging.domain.models.declare.QueueDeclarator;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import static com.example.poc.shared.messaging.domain.models.QueueAlias.OMS_ORDER_STATUS;
import static com.example.poc.shared.messaging.domain.models.connection.Connection.OMS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.ExchangeBuilder.headersExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

@Component
public class OrderStatusDeclarator implements QueueDeclarator {
  @Override
  public Connection connection() {
    return OMS;
  }

  @Override
  public QueueAlias alias() {
    return OMS_ORDER_STATUS;
  }

  @Override
  public Time retryDelay() {
    return Time.of(5, SECONDS);
  }

  @Override
  public void declare(RabbitAdmin admin) {
    Exchange externalExchange = headersExchange("notification.order.status.notification.v2.exchange")
      .build();

    Exchange exchange = directExchange("order.after-sale")
      .build();

    var orderStatusQueue = durable("order.after-sale")
      .withArgument("x-dead-letter-exchange", "order.after-sale")
      .withArgument("x-dead-letter-routing-key", "retry")
      .build();

    var oderStatusQueueRetry = durable("order.after-sale.retry")
      .withArgument("x-dead-letter-exchange", "order.after-sale")
      .withArgument("x-dead-letter-routing-key", "order-status")
      .withArgument("x-message-ttl", retryDelay().toMillis())
      .build();

    admin.declareExchange(externalExchange);
    admin.declareExchange(exchange);

    admin.declareQueue(orderStatusQueue);
    admin.declareQueue(oderStatusQueueRetry);

    admin.declareBinding(bind(orderStatusQueue).to(externalExchange).with("").noargs());

    admin.declareBinding(bind(orderStatusQueue).to(exchange).with("order-status").noargs());
    admin.declareBinding(bind(oderStatusQueueRetry).to(exchange).with("retry").noargs());
  }
}
