package com.example.poc.shared.messaging.services;

import com.example.poc.shared.messaging.domain.models.RetryListener;
import com.example.poc.shared.messaging.domain.models.RetryManager;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.poc.shared.messaging.domain.utils.RabbitAnnotationUtils.getConnection;
import static com.example.poc.shared.messaging.domain.utils.RabbitAnnotationUtils.getQueue;
import static java.lang.String.format;

@Slf4j
@Service
@AllArgsConstructor
public class ListenerManager {
  private final List<RetryListener<?>> listeners;
  private final ConnectionManager connections;
  private final QueueManager queues;
  private final Validator validator;
  
  public void startAllListeners() {    
    for (var listener : listeners) {
      var queueAlias = getQueue(listener);
      
      try {
        var connection = getConnection(listener);

        var queue = queues
          .getByAlias(queueAlias)
          .orElseThrow(() -> new RuntimeException(format("não foi possível localizar a queue: %s", queueAlias)));
        
        var factory = connections
          .getFactoryByConnection(connection)
          .orElseThrow(() -> new RuntimeException(format("não foi possível localizar a connection factory de: %s", connection)));
                
        var settings = connections.getSettingsByConnection(connection)
          .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações de: %s", connection)));
        
        var container = settings
          .getListenerSettings()
          .getCustomManualContainer(factory);
        
        queue.setQueueOnContainer(container);
        
        container.setMessageListener(new RetryManager<>(queue, listener, validator));
        
        container.start();        
      } catch (Exception exception) {
        log.error(
          "não foi possível fazer a configuração do listener {}, erro: {}",
          queueAlias,
          exception.getMessage()
        );
      }
    }
  }
}
