package com.example.poc.configuration.messaging.kafka.poststart;

import com.example.poc.shared.messaging.kafka.services.DeclareTopicsManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Slf4j
@Component
@AllArgsConstructor
public class TopicsConfiguration implements CommandLineRunner {
  private final DeclareTopicsManager manager;

  @Override
  public void run(String... args) {
    log.debug("declarando tópicos");
    manager.declareAllTopics();
  }
}
