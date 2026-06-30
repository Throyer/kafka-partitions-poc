package com.example.poc.modules.aftersale.messaging.producers;

import static com.example.poc.configuration.messaging.kafka.KafkaConfigurations.TOPIC_NAME;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.messaging.kafka.domain.models.TopicProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AfterSaleUpdateProducer extends TopicProducer<Event> {
  public AfterSaleUpdateProducer(
    @Qualifier("kafka-template-aftersale")
    KafkaTemplate<String, Event> template
  ) {
    super(TOPIC_NAME, template);
  }
}
