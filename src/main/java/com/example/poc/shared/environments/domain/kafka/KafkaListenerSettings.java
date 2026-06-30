package com.example.poc.shared.environments.domain.kafka;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.RECORD;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaListenerSettings {
  private Integer concurrency;

  public <K, V> ConcurrentKafkaListenerContainerFactory<K, V> getManualContainerFactory(
    ConsumerFactory<K, V> factory
  ) {
    var listener = new ConcurrentKafkaListenerContainerFactory<K, V>();
    
    listener.setConsumerFactory(factory);
    listener.setConcurrency(this.getConcurrency());
    
    listener
      .getContainerProperties()
      .setAckMode(RECORD);
    
    return listener;
  }
}
