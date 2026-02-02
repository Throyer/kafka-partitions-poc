package com.github.throyer.rabbitmq.consumer.configuration;

import com.github.throyer.rabbitmq.consumer.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

import static com.github.throyer.rabbitmq.consumer.utils.JSON.MAPPER;
import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;
import static org.springframework.amqp.core.QueueBuilder.durable;

@Slf4j
@Configuration
public class RabbitMQ {
  public static final String QUEUE_NAME_PATTERN = "after-sale.update-{0}";
  public static final int TOTAL_PARTITIONS = 32;
  
  @Bean("queues")
  String[] queues() {
    var queues = new String[TOTAL_PARTITIONS];    
    for (int index = 0; index < TOTAL_PARTITIONS; index++) {
      queues[index] = MessageFormat.format(QUEUE_NAME_PATTERN, index);
    }    
    return queues;
  }

  @Bean("container")
  public SimpleRabbitListenerContainerFactory factory(ConnectionFactory connection) {
    var container = new SimpleRabbitListenerContainerFactory();
    container.setMessageConverter(new Jackson2JsonMessageConverter(MAPPER));
    container.setConnectionFactory(connection);
    container.setPrefetchCount(1);
    container.setConcurrentConsumers(1);
    container.setDefaultRequeueRejected(false);
    container.setAcknowledgeMode(MANUAL);
    return container;
  }
}
