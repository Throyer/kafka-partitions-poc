package com.example.poc.shared.messaging.domain.queues;

import static com.example.poc.shared.messaging.domain.models.Connection.OMS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.ExchangeBuilder.headersExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.example.poc.shared.common.domain.models.Time;
import com.example.poc.shared.messaging.domain.models.Connection;
import com.example.poc.shared.messaging.domain.models.Queue;

@Component
public class OrderStatusQueue implements Queue {
  public static final String ORDER_STATUS_ALIAS = "order-status";
  
  private final RabbitTemplate template;

  public OrderStatusQueue(@Qualifier("oms-template") RabbitTemplate template) {
    this.template = template;
  }
  
  @Override
  public Connection connection() {
    return OMS;
  }

  @Override
  public String alias() {
    return ORDER_STATUS_ALIAS;
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

  @Override
  public void setQueue(SimpleMessageListenerContainer container) {
    container.setQueueNames("order.after-sale");
  }

  @Override
  public <T> void publish(T content) {
    template.convertAndSend("order.after-sale", "order-status", content);
  }
}
