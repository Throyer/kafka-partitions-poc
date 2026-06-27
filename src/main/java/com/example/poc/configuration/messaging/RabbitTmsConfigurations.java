package com.example.poc.configuration.messaging;

import static com.example.poc.shared.messaging.domain.models.Connection.TMS;
import static com.example.poc.shared.messaging.domain.utils.RabbitUtils.createTemplate;
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
import com.example.poc.shared.environments.domain.ConnectionsProperties;


@Configuration
public class RabbitTmsConfigurations {
  @Bean("tms-connection")
  CachingConnectionFactory connection(
    ConnectionsProperties properties
  ) throws NoSuchAlgorithmException, KeyManagementException {
    var connection = properties.getByConnection(TMS)
      .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações para %s", TMS)));
    
    return connection.toFactory();
  }

  @Bean(name = "tms-container")
  SimpleRabbitListenerContainerFactory container(
    @Qualifier("tms-connection") ConnectionFactory factory,
    ConnectionsProperties properties
  ) {
    var connection = properties.getByConnection(TMS)
      .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações para %s", TMS)));
    
    var settings = connection.getListenerSettings();
    return settings.getManualContainerFactory(factory);
  }

  @Bean(name = "tms-template")
  RabbitTemplate rabbitMQTemplate(
    @Qualifier("tms-connection") ConnectionFactory connection
  ) {
    return createTemplate(connection);
  }
}
