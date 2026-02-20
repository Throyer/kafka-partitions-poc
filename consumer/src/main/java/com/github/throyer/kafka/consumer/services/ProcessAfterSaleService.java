package com.github.throyer.kafka.consumer.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.github.throyer.kafka.consumer.models.AfterSale;
import com.github.throyer.kafka.consumer.models.AfterSaleEvent;
import com.github.throyer.kafka.consumer.models.Event;
import com.github.throyer.kafka.consumer.repositories.AfterSaleRepository;
import com.github.throyer.kafka.consumer.utils.JSON;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ProcessAfterSaleService {
  private final AfterSaleRepository repository;

  public void process(Event event) {
    var orderNumber = event.getOrderNumber();
    var afterSale = repository.findByOrderNumber(orderNumber)
      .orElse(null);
    
    if (Objects.nonNull(afterSale)) {
      var events = afterSale.getEvents();
      events.add(new AfterSaleEvent(event.getCode(), LocalDateTime.now()));
      log.debug("pedido: {}, atualizando.", JSON.stringify(event));
      
      repository.save(afterSale);
      return;
    }
    
    repository.save(new AfterSale(null, orderNumber, List.of(new AfterSaleEvent(event.getCode(), LocalDateTime.now()))));
    log.debug("pedido: {}, criado.", JSON.stringify(event));
  }
}
