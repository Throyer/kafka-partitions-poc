package com.example.poc.rabbitmq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  public static final String PEDIDOS_EXCHANGE = "orders-update";
  public static final String HASH_HEADER = "order-number";
  public static final int PARTITION_COUNT = 8;
  public static final String QUEUE_PREFIX = "orders-update-";

  public static final String[] QUEUE_NAMES =
    IntStream.range(0, PARTITION_COUNT).mapToObj(RabbitMqConfig::queueName).toArray(String[]::new);

  public static String queueName(int index) {
    return QUEUE_PREFIX + index;
  }

  @Bean
  CustomExchange pedidosExchange() {
    return new CustomExchange(PEDIDOS_EXCHANGE, "x-consistent-hash", true, false, Map.of("hash-header", HASH_HEADER));
  }

  @Bean
  Declarables pedidosTopology(CustomExchange pedidosExchange) {
    List<Declarable> declarables = new ArrayList<>();

    for (int i = 0; i < PARTITION_COUNT; i++) {
      Queue queue = new Queue(queueName(i), true, false, false, Map.of("x-single-active-consumer", true));

      Binding binding = BindingBuilder.bind(queue).to(pedidosExchange).with("1").noargs();

      declarables.add(queue);
      declarables.add(binding);
    }

    return new Declarables(declarables);
  }

  @Bean
  MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
