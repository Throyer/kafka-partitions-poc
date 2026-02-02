package com.github.throyer.rabbitmq.producer.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQ {
  @Bean
  RabbitAdmin admin(ConnectionFactory factory) {
    return new RabbitAdmin(factory);
  }
}
