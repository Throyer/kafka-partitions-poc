package com.example.poc.modules.aftersale.messaging.producers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.messaging.kafka.domain.models.TopicProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AfterSaleUpdateProducer extends TopicProducer<Event> {
  public AfterSaleUpdateProducer(
    @Value("${kafka.topics[0].topic-name}") String topic,
    @Qualifier("kafka-template-aftersale") KafkaTemplate<String, Event> template
  ) {
    super(topic, template);
  }
}
