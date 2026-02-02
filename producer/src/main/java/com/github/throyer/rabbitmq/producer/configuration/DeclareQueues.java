package com.github.throyer.rabbitmq.producer.configuration;

import com.github.throyer.rabbitmq.producer.models.Event;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Objects;

import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

@Component
@AllArgsConstructor
public class DeclareQueues implements CommandLineRunner {
  public static final String QUEUE_NAME_PATTERN = "after-sale.update-{0}";
  public static final String PARTITION_NAME_PATTERN = "after-sale.partition-{0}";
  
  public static final String EXCHANGE_NAME = "after-sale.update";
  public static final int TOTAL_PARTITIONS = 32;
  
  private final RabbitAdmin admin;
  
  @Override
  public void run(String... args) throws Exception {
    Exchange exchange = directExchange(EXCHANGE_NAME)
      .durable(true)
      .build();

    admin.declareExchange(exchange);
    
    for (int index = 0; index < TOTAL_PARTITIONS; index++) {
      var name = MessageFormat.format(QUEUE_NAME_PATTERN, index);
      var queue = durable(name)
        .singleActiveConsumer()
        .build();
      admin.declareQueue(queue);

      var partition = MessageFormat.format(PARTITION_NAME_PATTERN, index);
      
      var bind = bind(queue)
        .to(exchange)
        .with(partition)
        .noargs();
      
      admin.declareBinding(bind);
    }
  }
  
  public static String partition(Event event) {
    var index = Math.abs(Objects.hash(event.getOrderNumber())) % TOTAL_PARTITIONS;
    return MessageFormat.format(PARTITION_NAME_PATTERN, index);
  }
}
