package com.example.poc.shared.messaging.kafka.domain.models;

import com.example.poc.shared.common.domain.utils.JSON;
import com.example.poc.shared.environments.domain.kafka.KafkaListenerSettings;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Getter
@Setter
public class TopicSettings {
  private TopicAlias name;
  private String consumerGroupId;

  @NestedConfigurationProperty
  private KafkaListenerSettings listenerSettings;

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
