package com.example.poc.shared.messaging.services;

import java.util.HashMap;
import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;
import com.example.poc.shared.messaging.domain.models.connection.Connection;
import com.example.poc.shared.messaging.domain.models.declare.QueueDeclarator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class DeclareQueuesManager {
  private final List<QueueDeclarator> queues;
  private final ConnectionManager manager;
    
  public void declareAllQueues() {
    var connections = new HashMap<Connection, RabbitAdmin>();

    for (var name : List.of(Connection.values())) {
      manager.getFactoryByConnection(name)
        .map(RabbitAdmin::new)
        .ifPresent(admin -> connections.put(name, admin));
    }
    
    if (connections.isEmpty()) {
      log.warn("nenhuma connection encontrada.");
      return;
    }
    
    for (var queue : queues) {
      try {
        var connection = queue.connection();
        var alias = queue.alias();
                
        if (!connections.containsKey(connection)) {
          log.debug("connection {} não encontrada. queue: {}", connection, queue.alias());
          continue;
        }
        
        log.debug("declarando queue: {}", alias);
        queue.declare(connections.get(connection));
                
      } catch (Exception exception) {
        log.error(
          "não foi possível declarar {}, {}",
          queue.alias(),
          exception.getMessage()
        );
      }
    }
  }
}
