package com.github.throyer.kafka.consumer.configuration;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
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
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  ConsumerFactory<String, Event> consumerFactory() {
    var props = new HashMap<String, Object>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "after-sale-processor");
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
    
    return new DefaultKafkaConsumerFactory<>(
      props,
      new StringDeserializer(),
      new JsonDeserializer<>(Event.class)
    );
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, Event> kafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, Event>();
    factory.setConsumerFactory(consumerFactory());    
    return factory;
  }
}
