package com.example.poc.modules.aftersale.services;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.messaging.domain.models.message.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateAfterSaleService {  
  public void update(Message<Event> message) {
    var queue = message.consumerQueue();
    var event = message.getBody();
    
    log.info(
      "event received. queue: {}, orderNumber: {}, code: {}",
      queue,
      event.getOrderNumber(),
      event.getStatusCode()
    );
  }
}
