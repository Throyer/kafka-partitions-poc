package com.example.poc.shared.messaging.kafka.domain.publishers;

import static com.example.poc.shared.messaging.kafka.domain.declares.AfterSaleUpdateTopicDeclarator.TOPIC_NAME;
import static com.example.poc.shared.messaging.kafka.domain.models.TopicAlias.TRACKING_UPDATE_AFTERSALE;

import com.example.poc.shared.messaging.kafka.domain.models.TopicAlias;
import com.example.poc.shared.messaging.kafka.domain.models.publisher.TopicProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UpdateAfterSale extends TopicProducer {
  public UpdateAfterSale(
    @Qualifier("kafka-template-aftersale") KafkaTemplate<String, Object> template
  ) {
    super(template);
  }

  @Override
  public TopicAlias alias() {
    return TRACKING_UPDATE_AFTERSALE;
  }

  @Override
  public String topic() {
    return TOPIC_NAME;
  }
}
