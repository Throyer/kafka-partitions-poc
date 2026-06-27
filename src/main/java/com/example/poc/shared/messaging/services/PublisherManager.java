package com.example.poc.shared.messaging.services;

import java.util.List;

import com.example.poc.shared.messaging.domain.models.QueueAlias;
import com.example.poc.shared.messaging.domain.models.publisher.QueueProducer;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class PublisherManager {
  private final List<QueueProducer> publishers;
  
  public QueueProducer getByAlias(QueueAlias alias) {
    return publishers
      .stream()
      .filter(publisher -> publisher.hasAlias(alias))
      .findFirst()
      .orElseThrow(() -> new RuntimeException(format("não foi possível encontrar queue com alias: %s", alias)));
  }
}
