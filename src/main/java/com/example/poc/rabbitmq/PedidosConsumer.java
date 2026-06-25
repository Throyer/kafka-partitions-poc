package com.example.poc.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidosConsumer {

  private static final Logger log = LoggerFactory.getLogger(PedidosConsumer.class);

  @RabbitListener(queues = RabbitMqConfig.PEDIDOS)
  public void consume(String message) {
    log.info("Mensagem recebida na fila pedidos: {}", message);
  }
}
