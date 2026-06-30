package com.example.poc.shared.environments.domain;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import com.example.poc.shared.messaging.kafka.domain.models.KafkaListenerSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
  private String bootstrapServers;
  private String clientId;
  private String consumerGroupId;

  @NestedConfigurationProperty
  private KafkaListenerSettings listenerSettings;

  public DefaultKafkaProducerFactory<String, Object> toProducerFactory() {
    return new DefaultKafkaProducerFactory<>(toProducerConfigs());
  }

  public DefaultKafkaConsumerFactory<String, Object> toConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(toConsumerConfigs());
  }

  public Map<String, Object> toAdminConfigs() {
    var configs = new HashMap<String, Object>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    return configs;
  }

  public Map<String, Object> toProducerConfigs() {
    var configs = new HashMap<String, Object>();
    configs.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(CLIENT_ID_CONFIG, clientId);
    configs.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configs.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

    return configs;
  }

  public Map<String, Object> toConsumerConfigs() {
    var configs = new HashMap<String, Object>();
    configs.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(GROUP_ID_CONFIG, consumerGroupId);
    configs.put(CLIENT_ID_CONFIG, clientId);
    configs.put(ENABLE_AUTO_COMMIT_CONFIG, false);
    configs.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    configs.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Object.class.getName());
    configs.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

    return configs;
  }
}
