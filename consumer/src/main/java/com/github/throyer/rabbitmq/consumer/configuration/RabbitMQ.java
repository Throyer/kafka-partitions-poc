package com.github.throyer.rabbitmq.consumer.configuration;

import static com.github.throyer.rabbitmq.consumer.utils.JSON.MAPPER;
import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMQ {  
  @Bean("queues")
  String[] queues(EventsQueue eventsQueue) {
    return eventsQueue.partitionedQueues();
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
