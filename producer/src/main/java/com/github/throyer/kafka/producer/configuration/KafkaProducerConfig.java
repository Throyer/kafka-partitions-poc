package com.github.throyer.kafka.producer.configuration;

import java.util.HashMap;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.github.throyer.kafka.producer.models.Event;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  KafkaAdmin kafkaAdmin() {
    var configs = new HashMap<String, Object>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    return new KafkaAdmin(configs);
  }

  @Bean
  NewTopic orderEventsTopic() {
    log.info("Creating Kafka topic: {} with {} partitions", "order-events", 6);
    return TopicBuilder.name("order-events")
        .partitions(6)
        .replicas(1)
        .build();
  }

  @Bean
  ProducerFactory<String, Event> producerFactory() {
    var props = new HashMap<String, Object>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  KafkaTemplate<String, Event> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
