package com.github.throyer.rabbitmq.consumer.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

import static org.springframework.amqp.core.QueueBuilder.durable;

@Slf4j
@Configuration
public class RabbitMQ {
  public static final String QUEUE_NAME_PATTERN = "after-sale.update-{0}";
  public static final int TOTAL_PARTITIONS = 32;
  
  @Bean("queues")
  Queue[] queues() {
    var queues = new Queue[TOTAL_PARTITIONS];
    
    for (int index = 0; index < TOTAL_PARTITIONS; index++) {
      var name = MessageFormat.format(QUEUE_NAME_PATTERN, index);
      var queue = durable(name)
        .singleActiveConsumer()
        .build();
      
      queues[index] = queue;
    }
    
    return queues;
  }
}
