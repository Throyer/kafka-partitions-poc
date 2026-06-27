package com.example.poc.configuration.messaging.poststart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.example.poc.shared.messaging.services.ListenerManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(2)
@Slf4j
@Component
@AllArgsConstructor
public class ListenersConfiguration implements CommandLineRunner {
  private final ListenerManager manager;

  @Override
  public void run(String... args) throws Exception {
    log.debug("iniciando listeners");
    manager.startAllListeners();
  }
}
