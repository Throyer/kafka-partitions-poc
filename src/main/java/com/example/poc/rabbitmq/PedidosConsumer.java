package com.example.poc.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PedidosConsumer {
  @RabbitListener(queues = "#{T(com.example.poc.rabbitmq.RabbitMqConfig).QUEUE_NAMES}")
  public void consume(
    Order order,
    @Header(AmqpHeaders.CONSUMER_QUEUE) String queue,
    @Header(name = RabbitMqConfig.HASH_HEADER) String orderNumberHeader
  ) {
    log.info(
      "Pedido recebido: queue={}, header {}={}, orderNumber={}, user={}",
      queue,
      RabbitMqConfig.HASH_HEADER,
      orderNumberHeader,
      order.orderNumber(),
      order.user()
    );
  }
}
