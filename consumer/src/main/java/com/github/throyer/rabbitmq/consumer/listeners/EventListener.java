package com.github.throyer.rabbitmq.consumer.listeners;

import static org.springframework.amqp.support.AmqpHeaders.DELIVERY_TAG;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.consumer.models.Event;
import com.github.throyer.rabbitmq.consumer.utils.JSON;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EventListener {
  @RabbitListener(queues = "#{@queues}")
  public void processar(Event event, Channel channel, @Header(DELIVERY_TAG) long tag) throws IOException {
    try {
      log.debug("processando pedido: {}", JSON.stringify(event));
      channel.basicAck(tag, false);
    } catch (Exception IOException) {
      channel.basicNack(tag, false, true);
    }
  }
}
