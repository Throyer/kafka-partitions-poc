package com.example.poc.configuration.messaging.rabbitmq.poststart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.example.poc.shared.messaging.rabbitmq.services.DeclareQueuesManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Slf4j
@Component
@AllArgsConstructor
public class QueuesConfiguration implements CommandLineRunner {
  private final DeclareQueuesManager manager;

  @Override
  public void run(String... args) throws Exception {
    log.debug("declarando filas");
    manager.declareAllQueues();
  }
}
