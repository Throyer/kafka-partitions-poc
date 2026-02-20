package com.github.throyer.kafka.consumer.listeners;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.throyer.kafka.consumer.models.AfterSale;
import com.github.throyer.kafka.consumer.models.AfterSaleEvent;
import com.github.throyer.kafka.consumer.models.Event;
import com.github.throyer.kafka.consumer.repositories.EventRepository;
import com.github.throyer.kafka.consumer.utils.JSON;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EventListener {
  private final EventRepository repository;
  
  @KafkaListener(
    topics = "order-events",
    groupId = "after-sale-processor",
    containerFactory = "kafkaListenerContainerFactory"
  )
  public void processar(
    @Payload Event event,
    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
  ) {
    log.debug("event: {}, partição: {}, recebido.", JSON.stringify(event), partition);

    var orderNumber = event.getOrderNumber();
    var afterSale = repository.findByOrderNumber(orderNumber)
      .orElse(null);
    
    if (Objects.nonNull(afterSale)) {
      var events = afterSale.getEvents();
      events.add(new AfterSaleEvent(event.getCode(), LocalDateTime.now()));
      log.debug("pedido: {}, partição: {}, atualizando.", JSON.stringify(event), partition);
      
      repository.save(afterSale);
      return;
    }
    
    repository.save(new AfterSale(null, orderNumber, List.of(new AfterSaleEvent(event.getCode(), LocalDateTime.now()))));
    log.debug("pedido: {}, partição: {}, criado.", JSON.stringify(event), partition);
  }
}
