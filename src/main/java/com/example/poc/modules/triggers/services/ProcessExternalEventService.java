package com.example.poc.modules.triggers.services;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.aftersale.messaging.producers.AfterSaleUpdateProducer;
import com.example.poc.modules.triggers.domain.models.ExternalTriggerEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProcessExternalEventService {
  private final AfterSaleUpdateProducer producer;
  
  public void process(ExternalTriggerEvent external) {    
    var event = new Event(
      external.orderNumber(),
      external.statusCode(), 
      external.trigger(),
      external.timestamp()
    );
    
    producer.publish(event);
  }
}
