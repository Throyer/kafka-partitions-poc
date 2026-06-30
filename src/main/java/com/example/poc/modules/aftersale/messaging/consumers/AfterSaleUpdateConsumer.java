package com.example.poc.modules.aftersale.messaging.consumers;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_PARTITION;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TOPIC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.updaters.services.UpdateAfterSaleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class AfterSaleUpdateConsumer {
  private final UpdateAfterSaleService service;

  @KafkaListener(
    topics = "${kafka.topics[0].topic-name}",
    containerFactory = "kafka-container-aftersale"
  )
  public void update(
    @Payload Event event,
    @Header(RECEIVED_TOPIC) String topic,
    @Header(RECEIVED_KEY) String key,
    @Header(RECEIVED_PARTITION) int partition
  ) {
    log.info(
      "event received. topic: {}, partition: {}, orderNumber: {}, code: {}",
      topic,
      partition,
      key,
      event.getStatusCode()
    );

    service.update(event);
  }
}
