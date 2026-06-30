package com.example.poc.configuration.messaging.kafka;

import static com.example.poc.shared.messaging.kafka.domain.utils.KafkaUtils.createTemplate;

import com.example.poc.shared.environments.domain.KafkaProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfigurations {
  @Bean("kafka-producer")
  ProducerFactory<String, Object> producer(KafkaProperties properties) {
    return properties.toProducerFactory();
  }

  @Bean("kafka-consumer")
  ConsumerFactory<String, Object> consumer(KafkaProperties properties) {
    return properties.toConsumerFactory();
  }

  @Bean(name = "kafka-container")
  ConcurrentKafkaListenerContainerFactory<String, Object> container(
    @Qualifier("kafka-consumer") ConsumerFactory<String, Object> factory,
    KafkaProperties properties
  ) {
    return properties.getListenerSettings().getManualContainerFactory(factory);
  }

  @Bean(name = "kafka-template")
  KafkaTemplate<String, Object> template(
    @Qualifier("kafka-producer") ProducerFactory<String, Object> producer
  ) {
    return createTemplate(producer);
  }
}
