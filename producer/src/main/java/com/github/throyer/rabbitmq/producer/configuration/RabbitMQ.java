package com.github.throyer.rabbitmq.producer.configuration;

import static com.github.throyer.rabbitmq.producer.utils.JSON.MAPPER;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMQ {
  @Bean
  RabbitAdmin admin(ConnectionFactory factory) {
    return new RabbitAdmin(factory);
  }

  @Bean
  RabbitTemplate createTemplate(ConnectionFactory factory) {
    var template = new RabbitTemplate(factory);
    template.setMessageConverter(new Jackson2JsonMessageConverter(MAPPER));
    return template;
  }
}
