package com.github.throyer.rabbitmq.streams.configuration;

import static com.rabbitmq.stream.ByteCapacity.from;
import static java.time.Duration.ofMinutes;

import java.util.function.Consumer;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.rabbit.stream.support.StreamAdmin;

import com.github.throyer.rabbitmq.streams.models.Event;
import com.github.throyer.rabbitmq.streams.utils.JSON;
import com.rabbitmq.stream.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMQStreamConfig {
  @Bean
  RabbitAdmin admin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  StreamAdmin stream(Environment environment) {
    return new StreamAdmin(environment, creator ->
      creator.stream("events")
        .maxAge(ofMinutes(1))
        .maxSegmentSizeBytes(from("100MB"))
        .superStream()
        .partitions(3));
  }

  @Bean
  Consumer<Message<Event>> input() {
    return (message) -> log.debug("mensagem recebida: {}", JSON.stringify(message.getPayload()));
  }
}
