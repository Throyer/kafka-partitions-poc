package com.example.poc.shared.environments.domain.kafka;

import static java.lang.String.format;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import java.util.HashMap;
import java.util.Set;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;
import com.example.poc.shared.messaging.kafka.domain.models.TopicAlias;
import com.example.poc.shared.messaging.kafka.domain.models.TopicSettings;
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
  
  public TopicSettings requireByAlias(TopicAlias alias) {
    return topics
      .stream()
      .filter(topic -> topic.getName().equals(alias))
      .findFirst()
      .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações para %s", alias)));
  }

  public KafkaAdmin admin() {
    var configs = new HashMap<String, Object>();

    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    return new KafkaAdmin(configs);
  }

  public <T> DefaultKafkaProducerFactory<String, T> producer(TopicAlias alias) {
    var configs = new HashMap<String, Object>();
    configs.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(CLIENT_ID_CONFIG, clientId);
    configs.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    
    return requireByAlias(alias)
      .producer(configs);
  }

  public <T> DefaultKafkaConsumerFactory<String, T> consumer(TopicAlias alias, Class<T> type) {
    var topic = requireByAlias(alias);
    var configs = new HashMap<String, Object>();

    configs.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(GROUP_ID_CONFIG, topic.getConsumerGroupId());
    configs.put(CLIENT_ID_CONFIG, clientId);
    configs.put(ENABLE_AUTO_COMMIT_CONFIG, false);
    configs.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    configs.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return topic.consumer(configs, type);
  }
}
