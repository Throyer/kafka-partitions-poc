package com.github.throyer.rabbitmq.consumer.listeners;

import static org.springframework.amqp.support.AmqpHeaders.DELIVERY_TAG;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.consumer.models.AfterSale;
import com.github.throyer.rabbitmq.consumer.models.AfterSaleEvent;
import com.github.throyer.rabbitmq.consumer.models.Event;
import com.github.throyer.rabbitmq.consumer.repositories.EventRepository;
import com.github.throyer.rabbitmq.consumer.utils.JSON;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EventListener {
  private final EventRepository repository;
  
  @RabbitListener(queues = "#{@queues}", containerFactory = "container")
  public void processar(Event event, Channel channel, @Header(DELIVERY_TAG) long tag) throws IOException {
    try {
      var orderNumber = event.getOrderNumber();
      var afterSale = repository.findByOrderNumber(orderNumber)
        .orElse(null);
      
      if (Objects.nonNull(afterSale)) {
        var events = afterSale.getEvents();
        events.add(new AfterSaleEvent(event.getCode(), LocalDateTime.now()));
        log.debug("pedido: {}, processado.", JSON.stringify(event));
        
        repository.save(afterSale);
        channel.basicAck(tag, false);
        return;
      }
      
      repository.save(new AfterSale(null, orderNumber, List.of(new AfterSaleEvent(event.getCode(), LocalDateTime.now()))));
      log.debug("pedido: {}, processado.", JSON.stringify(event));
      
      channel.basicAck(tag, false);
    } catch (Exception IOException) {
      channel.basicNack(tag, false, true);
    }
  }
}
