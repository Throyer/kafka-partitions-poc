package com.github.throyer.kafka.producer.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.github.throyer.kafka.producer.models.Event;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class KafkaProducerConfiguration {
  @Bean
  KafkaAdmin kafkaAdmin(KafkaSettings settings) {
    return new KafkaAdmin(settings.toMap());
  }

  @Bean
  NewTopic topic(KafkaSettings settings) {    
    return TopicBuilder.name(settings.getTopic())
      .partitions(settings.getPartitions())
      .build();
  }

  @Bean
  ProducerFactory<String, Event> producer(KafkaSettings settings) {
    return new DefaultKafkaProducerFactory<>(settings.toMap());
  }

  @Bean
  KafkaTemplate<String, Event> kafkaTemplate(ProducerFactory<String, Event> producer) {
    return new KafkaTemplate<>(producer);
  }
}
