package com.example.poc.modules.aftersale.messaging.consumers;

import static com.example.poc.shared.messaging.kafka.domain.declares.AfterSaleUpdateTopicDeclarator.TOPIC_NAME;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.updaters.services.UpdateAfterSaleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class AfterSaleUpdateConsumer {
  private final UpdateAfterSaleService service;

  @KafkaListener(
    topics = TOPIC_NAME,
    containerFactory = "kafka-container"
  )
  public void listen(
    @Payload Event event,
    @Header(KafkaHeaders.RECEIVED_KEY) String orderNumber,
    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
  ) {
    log.info(
      "event received. topic: {}, partition: {}, orderNumber: {}, code: {}",
      TOPIC_NAME,
      partition,
      orderNumber,
      event.getStatusCode()
    );

    service.update(event);
  }
}
