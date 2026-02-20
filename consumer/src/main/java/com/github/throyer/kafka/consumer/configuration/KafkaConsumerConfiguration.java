package com.github.throyer.kafka.consumer.configuration;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.github.throyer.kafka.consumer.models.Event;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {
  @Bean("kafka-consumer-factory")
  ConsumerFactory<String, Event> consumer(KafkaSettings settings) {
    return new DefaultKafkaConsumerFactory<>(
      settings.map(),
      new StringDeserializer(),
      new JsonDeserializer<>(Event.class)
    );
  }

  @Bean("kafka-listener-container")
  ConcurrentKafkaListenerContainerFactory<String, Event> container(
    @Qualifier("kafka-consumer-factory") ConsumerFactory<String, Event> consumer
  ) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, Event>();
    factory.setConsumerFactory(consumer);    
    return factory;
  }
}
