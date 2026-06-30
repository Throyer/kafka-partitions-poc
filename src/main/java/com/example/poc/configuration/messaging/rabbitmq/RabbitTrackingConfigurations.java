package com.example.poc.configuration.messaging.rabbitmq;

import static com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection.TRACKING;
import static com.example.poc.shared.messaging.rabbitmq.domain.utils.RabbitUtils.createTemplate;
import static java.lang.String.format;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.poc.shared.environments.domain.rabbitmq.ConnectionsProperties;

@Configuration
public class RabbitTrackingConfigurations {
  @Bean("tracking-connection")
  CachingConnectionFactory connection(
    ConnectionsProperties properties
  ) throws NoSuchAlgorithmException, KeyManagementException {
    var connection = properties.getByConnection(TRACKING)
      .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações para %s", TRACKING)));
    
    return connection.toFactory();
  }

  @Bean(name = "tracking-container")
  SimpleRabbitListenerContainerFactory container(
    @Qualifier("tracking-connection") ConnectionFactory factory,
    ConnectionsProperties properties
  ) {
    var connection = properties.getByConnection(TRACKING)
      .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações para %s", TRACKING)));
    
    var settings = connection.getListenerSettings();
    return settings.getManualContainerFactory(factory);
  }

  @Bean(name = "tracking-template")
  RabbitTemplate rabbitMQTemplate(
    @Qualifier("tracking-connection") ConnectionFactory connection
  ) {
    return createTemplate(connection);
  }
}
