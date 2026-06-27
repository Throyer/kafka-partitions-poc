package com.example.poc.modules.aftersale.messaging.producers;

import static com.example.poc.shared.messaging.domain.queues.AfterSaleUpdateQueue.AFTER_SALE_UPDATE_ALIAS;
import static com.example.poc.shared.messaging.domain.queues.AfterSaleUpdateQueue.HASH_HEADER;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.messaging.services.QueueManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AfterSaleUpdateProducer {
  private final QueueManager manager;
  
  public void publish(Event event) {
    var orderNumber = event.getOrderNumber();
    var headers = Map.of(HASH_HEADER, orderNumber);
    
    manager.getByAlias(AFTER_SALE_UPDATE_ALIAS)
      .ifPresent(queue -> queue.publish(event, headers));
  }
}
