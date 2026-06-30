package com.example.poc.modules.aftersale.messaging.consumers;

import static com.example.poc.configuration.messaging.kafka.KafkaConfigurations.TOPIC_NAME;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_PARTITION;
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
    topics = TOPIC_NAME,
    containerFactory = "kafka-container-aftersale"
  )
  public void update(
    @Payload Event event,
    @Header(RECEIVED_KEY) String key,
    @Header(RECEIVED_PARTITION) int partition
  ) {
    log.info(
      "event received. topic: {}, partition: {}, orderNumber: {}, code: {}",
      TOPIC_NAME,
      partition,
      key,
      event.getStatusCode()
    );

    service.update(event);
  }
}
