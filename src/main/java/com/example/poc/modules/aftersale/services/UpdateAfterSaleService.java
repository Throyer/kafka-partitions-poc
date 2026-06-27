package com.example.poc.modules.aftersale.services;

import com.example.poc.modules.aftersale.domain.models.AfterSale;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.aftersale.domain.models.ReceivedEvent;
import com.example.poc.modules.aftersale.repositories.AfterSaleRepository;
import com.example.poc.modules.aftersale.repositories.ReceivedEventRepository;
import com.example.poc.shared.messaging.domain.models.message.Message;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateAfterSaleService {
  private final AfterSaleRepository afterSaleRepository;
  private final ReceivedEventRepository receivedEventRepository;

  public void update(Message<Event> message) {
    var queue = message.consumerQueue();
    var event = message.getBody();
    var orderNumber = event.getOrderNumber();

    log.info(
      "event received. queue: {}, orderNumber: {}, code: {}",
      queue,
      orderNumber,
      event.getStatusCode()
    );

    afterSaleRepository
      .findByOrderNumber(orderNumber)
      .orElseGet(() -> afterSaleRepository.save(new AfterSale(null, orderNumber)));

    receivedEventRepository.save(
      new ReceivedEvent(null, orderNumber, event, LocalDateTime.now())
    );
  }
}
