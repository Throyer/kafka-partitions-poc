package com.github.throyer.rabbitmq.streams.configuration;

import java.time.Duration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.support.StreamAdmin;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMQStreamConfig {

  @Value("${rabbitmq.stream.name:events.stream}")
  private String streamName;
  
  @Value("${rabbitmq.stream.partitions:2}")
  private int partitions;

  @Bean
  MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  StreamAdmin streamAdmin(Environment environment) {
      return new StreamAdmin(environment, creator -> {
          log.info("Criando Super Stream: {} com {} partições", streamName, partitions);
          creator.stream(streamName)
              .maxAge(Duration.ofMinutes(5))
              .maxSegmentSizeBytes(ByteCapacity.from("100MB"))
              .superStream()
                  .partitions(partitions)
                  .creator()
              .create();
          log.info("Super Stream criada com sucesso: {}", streamName);
      });
  }
}
