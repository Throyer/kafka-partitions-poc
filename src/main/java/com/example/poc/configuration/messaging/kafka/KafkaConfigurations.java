package com.example.poc.configuration.messaging.kafka;

import static com.example.poc.shared.messaging.kafka.domain.utils.KafkaUtils.createTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.environments.domain.KafkaProperties;

@EnableKafka
@Configuration
public class KafkaConfigurations {
  @Bean
  KafkaAdmin kafkaAdmin(KafkaProperties properties) {
    return new KafkaAdmin(properties.toAdminConfigs());
  }

  @Bean("kafka-producer-aftersale")
  ProducerFactory<String, Event> producer(KafkaProperties properties) {
    return properties.toProducerFactory();
  }

  @Bean("kafka-consumer-aftersale")
  ConsumerFactory<String, Event> consumer(KafkaProperties properties) {
    return properties.toConsumerFactory(Event.class);
  }

  @Bean(name = "kafka-container-aftersale")
  ConcurrentKafkaListenerContainerFactory<String, Event> container(
    @Qualifier("kafka-consumer-aftersale") ConsumerFactory<String, Event> factory,
    KafkaProperties properties
  ) {
    return properties.getListenerSettings().getManualContainerFactory(factory);
  }

  @Bean(name = "kafka-template-aftersale")
  KafkaTemplate<String, Event> template(
    @Qualifier("kafka-producer-aftersale") ProducerFactory<String, Event> producer
  ) {
    return createTemplate(producer);
  }
}
