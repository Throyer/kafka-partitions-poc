package com.example.poc.shared.messaging.kafka.domain.models;

import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.example.poc.shared.common.domain.utils.JSON;
import com.example.poc.shared.environments.domain.kafka.KafkaListenerSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicSettings {
  private TopicAlias name;
  private String topicName;
  private String consumerGroupId;
  private int partitionCount;
  private short replicationFactor;

  @NestedConfigurationProperty
  private KafkaListenerSettings listenerSettings;

  public NewTopic topic() {
    return new NewTopic(topicName, partitionCount, replicationFactor);
  }

  public <T> DefaultKafkaProducerFactory<String, T> producer(Map<String, Object> configs) {
    var serializer = new JsonSerializer<T>(JSON.MAPPER);
    serializer.setAddTypeInfo(false);

    return new DefaultKafkaProducerFactory<>(
      configs,
      new StringSerializer(),
      serializer
    );
  }

  public <T> DefaultKafkaConsumerFactory<String, T> consumer(Map<String, Object> configs, Class<T> type) {
    var deserializer = new JsonDeserializer<>(type, JSON.MAPPER);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeHeaders(false);

    return new DefaultKafkaConsumerFactory<>(
      configs,
      new StringDeserializer(),
      deserializer
    );
  }
}
