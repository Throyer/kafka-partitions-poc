package com.github.throyer.rabbitmq.streams.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

import com.rabbitmq.stream.Environment;

@Configuration
public class RabbitMQStreamConfig {

  private static final String STREAM_NAME = "events-stream";

  @Bean
  MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  Queue eventsStream() {
    return QueueBuilder.durable(STREAM_NAME)
        .stream()
        .build();
  }

  @Bean
  RabbitStreamTemplate rabbitStreamTemplate(Environment environment) {
    RabbitStreamTemplate template = new RabbitStreamTemplate(environment, STREAM_NAME);
    template.setMessageConverter(messageConverter());
    return template;
  }
}
