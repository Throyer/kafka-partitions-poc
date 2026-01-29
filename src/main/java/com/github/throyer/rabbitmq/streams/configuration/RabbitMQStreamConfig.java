package com.github.throyer.rabbitmq.streams.configuration;

import java.time.Duration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.support.StreamAdmin;

import com.rabbitmq.stream.ByteCapacity;
import com.rabbitmq.stream.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMQStreamConfig {

  private static final String STREAM_NAME = "events.stream";

  @Bean
  MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  // @Bean
  // SuperStream eventsSuperStream() {
  //   return SuperStreamBuilder
  //       .superStream(STREAM_NAME, 2)
  //       .maxAge("5m")
  //       .maxSegmentSize(104857600)
  //       .build();
  // }

  @Bean
  StreamAdmin streamAdmin(Environment environment) {
      return new StreamAdmin(environment, creator -> {
          log.info("tentando fazer o role");
          creator.stream(STREAM_NAME)  // "events.stream"
              .maxAge(Duration.ofMinutes(5))
              .maxSegmentSizeBytes(ByteCapacity.from("100MB"))
              .superStream()              // ← Configura como Super Stream
                  .partitions(2)          // ← Define 2 partições
                  .creator()              // ← Volta para o creator
              .create();                  // ← Cria no RabbitMQ
      });
  }
}
