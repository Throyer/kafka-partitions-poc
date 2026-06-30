package com.example.poc.modules.aftersale.messaging.producers;

import static com.example.poc.shared.messaging.kafka.domain.models.TopicAlias.TRACKING_UPDATE_AFTERSALE;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.messaging.kafka.services.TopicPublisherManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AfterSaleUpdateProducer {
  private final TopicPublisherManager manager;

  public void publish(Event event) {
    manager.getByAlias(TRACKING_UPDATE_AFTERSALE)
      .publish(event, event.getOrderNumber());
  }
}
