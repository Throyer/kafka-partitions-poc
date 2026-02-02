package com.github.throyer.rabbitmq.producer.configuration;

import static com.github.throyer.rabbitmq.producer.configuration.DeclareQueues.PARTITION_NAME_PATTERN;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.github.throyer.rabbitmq.producer.models.Event;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ConfigurationProperties(prefix = "events.queue")
public class EventsQueue {  
  private Integer totalPartitions;

  private Integer getTotalPartitions() {
    return Optional.ofNullable(totalPartitions)
      .orElse(32);
  }
  
  public int partitions() {
    return getTotalPartitions();
  }

  public String partition(Event event) {
    var index = Math.abs(Objects.hash(event.getOrderNumber())) % partitions();
    return MessageFormat.format(PARTITION_NAME_PATTERN, index);
  }
}
