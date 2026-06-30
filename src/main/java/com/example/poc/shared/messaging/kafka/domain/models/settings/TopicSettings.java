package com.example.poc.shared.messaging.kafka.domain.models.settings;

import org.springframework.boot.context.properties.NestedConfigurationProperty;
import com.example.poc.shared.messaging.kafka.domain.models.KafkaListenerSettings;
import com.example.poc.shared.messaging.kafka.domain.models.TopicAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicSettings {
  private TopicAlias name;
  private String consumerGroupId;

  @NestedConfigurationProperty
  private KafkaListenerSettings listenerSettings;
}
