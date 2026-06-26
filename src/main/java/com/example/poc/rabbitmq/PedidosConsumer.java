package com.example.poc.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PedidosConsumer {
  @RabbitListener(queues = "#{T(com.example.poc.rabbitmq.RabbitMqConfig).QUEUE_NAMES}")
  public void consume(Order order) {
    log.info("Pedido recebido: orderNumber={}, user={}", order.orderNumber(), order.user());
  }
}
