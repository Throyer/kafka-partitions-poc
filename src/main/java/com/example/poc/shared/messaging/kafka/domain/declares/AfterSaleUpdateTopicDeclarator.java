package com.example.poc.shared.messaging.kafka.domain.declares;

import static com.example.poc.shared.messaging.kafka.domain.models.TopicAlias.TRACKING_UPDATE_AFTERSALE;

import com.example.poc.shared.messaging.kafka.domain.models.TopicAlias;
import com.example.poc.shared.messaging.kafka.domain.models.declare.TopicDeclarator;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
public class AfterSaleUpdateTopicDeclarator implements TopicDeclarator {
  public static final int PARTITION_COUNT = 8;
  public static final short REPLICATION_FACTOR = 1;

  public static final String TOPIC_NAME = "after-sale-update";
  public static final String MESSAGE_KEY = "order-number";

  @Override
  public TopicAlias alias() {
    return TRACKING_UPDATE_AFTERSALE;
  }

  @Override
  public void declare(KafkaAdmin admin) {
    var topic = new NewTopic(
      TOPIC_NAME,
      PARTITION_COUNT,
      REPLICATION_FACTOR
    );
    
    admin.createOrModifyTopics(topic);
  }
}
