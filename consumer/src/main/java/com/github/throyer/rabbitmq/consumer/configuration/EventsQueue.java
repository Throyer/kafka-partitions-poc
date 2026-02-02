package com.github.throyer.rabbitmq.consumer.configuration;

import com.github.throyer.rabbitmq.consumer.utils.Networks;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ConfigurationProperties(prefix = "events.queue")
public class EventsQueue {
  private static final String QUEUE_NAME_PATTERN = "queue.after-sale.update-{0}";
  
  private Integer totalPartitions;
  private Integer expectedReplicas;

  private Integer getTotalPartitions() {
    return Optional.ofNullable(totalPartitions)
      .orElse(32);
  }

  private Integer getExpectedReplicas() {
    return Optional.ofNullable(expectedReplicas)
      .orElse(1);
  }
  
  public int replicas() {
    return getExpectedReplicas();
  }
  
  public int partitions() {
    return getTotalPartitions();
  }

  public String[] partitionedQueues() {
    var queues = new ArrayList<String>();
    var range = Math.abs(Networks.hostname().hashCode()) % replicas();

    for (int partition = 0; partition < partitions(); partition++) {
      if (partition % replicas() == range) {
        queues.add(format(partition));
      }
    }

    return queues.toArray(new String[0]);
  }
  
  public static String format(int partition) {
    return MessageFormat.format(QUEUE_NAME_PATTERN, partition);
  }
}
