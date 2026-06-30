package com.example.poc.shared.messaging.rabbitmq.services;

import com.example.poc.shared.messaging.rabbitmq.domain.models.message.RetryListener;
import com.example.poc.shared.messaging.rabbitmq.domain.models.message.RetryManager;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.poc.shared.messaging.rabbitmq.domain.utils.RabbitAnnotationUtils.getConnection;
import static com.example.poc.shared.messaging.rabbitmq.domain.utils.RabbitAnnotationUtils.getQueue;
import static java.lang.String.format;

@Slf4j
@Service
@AllArgsConstructor
public class QueueListenerManager {
  private final List<RetryListener<?>> listeners;
  private final RabbitMqConnectionManager connections;
  private final QueueSettingsManager settingsManager;
  private final Validator validator;
  private final GenericApplicationContext context;
  
  public void startAllListeners() {
    if (listeners.isEmpty()) {
      log.warn("nenhum listeners para iniciar.");
      return;
    }
    
    for (var listener : listeners) {
      var queueAlias = getQueue(listener);
      
      try {
        var connection = getConnection(listener);

        var queueSettings = settingsManager
          .getByAlias(queueAlias)
          .orElseThrow(() -> new RuntimeException(format("não foi possível localizar a queue: %s", queueAlias)));
        
        var factory = connections
          .getFactoryByConnection(connection)
          .orElseThrow(() -> new RuntimeException(format("não foi possível localizar a connection factory de: %s", connection)));
                
        var connectionSettings = connections.getSettingsByConnection(connection)
          .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações de: %s", connection)));
        
        var container = connectionSettings
          .getListenerSettings()
          .getCustomManualContainer(factory);
        
        queueSettings.setQueue(container);
        
        container.setMessageListener(new RetryManager<>(queueSettings, listener, validator));

        var name = format("%s-rabbitmq-listener-%s", connection.name().toLowerCase(), queueAlias.getDescription());

        context.registerBean(name, SimpleMessageListenerContainer.class, () -> container);

        log.debug("iniciando listener: {}", name);
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
