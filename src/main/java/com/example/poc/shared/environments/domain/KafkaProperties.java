package com.example.poc.shared.environments.domain;

import static java.lang.String.format;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.messaging.kafka.domain.models.TopicAlias;
import com.example.poc.shared.messaging.kafka.domain.models.settings.TopicSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
  private String bootstrapServers;
  private String clientId;
  private Set<TopicSettings> topics;

  public Optional<TopicSettings> getByAlias(TopicAlias alias) {
    return topics
      .stream()
      .filter(topic -> topic.getName().equals(alias))
      .findFirst();
  }

  public DefaultKafkaProducerFactory<String, Event> toProducerFactory() {
    return new DefaultKafkaProducerFactory<>(toProducerConfigs());
  }

  public DefaultKafkaConsumerFactory<String, Event> toConsumerFactory(TopicSettings topic) {
    return toConsumerFactory(Event.class, topic);
  }

  public <T> DefaultKafkaConsumerFactory<String, T> toConsumerFactory(Class<T> type, TopicSettings topic) {
    var deserializer = new JsonDeserializer<>(type);
    deserializer.addTrustedPackages("*");
    deserializer.setUseTypeHeaders(false);
    return new DefaultKafkaConsumerFactory<>(toConsumerConfigs(topic), new StringDeserializer(), deserializer);
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

  public Map<String, Object> toConsumerConfigs(TopicSettings topic) {
    var configs = new HashMap<String, Object>();
    configs.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(GROUP_ID_CONFIG, topic.getConsumerGroupId());
    configs.put(CLIENT_ID_CONFIG, clientId);
    configs.put(ENABLE_AUTO_COMMIT_CONFIG, false);
    configs.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    configs.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return configs;
  }

  public TopicSettings requireByAlias(TopicAlias alias) {
    return getByAlias(alias)
      .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações para %s", alias)));
  }
}
