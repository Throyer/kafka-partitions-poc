package com.example.poc.configuration.messaging;

import com.example.poc.shared.environments.domain.ConnectionsProperties;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection.OMS;
import static com.example.poc.shared.messaging.rabbitmq.domain.utils.RabbitUtils.createTemplate;
import static java.lang.String.format;

@Configuration
public class RabbitOrderConfigurations {
  @Bean("oms-connection")
  CachingConnectionFactory connection(
    ConnectionsProperties properties
  ) throws NoSuchAlgorithmException, KeyManagementException {
    var connection = properties.getByConnection(OMS)
      .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações para %s", OMS)));
    
    return connection.toFactory();
  }

  @Bean(name = "oms-container")
  SimpleRabbitListenerContainerFactory container(
    @Qualifier("oms-connection") ConnectionFactory factory,
    ConnectionsProperties properties
  ) {
    var connection = properties.getByConnection(OMS)
      .orElseThrow(() -> new RuntimeException(format("não foi possível localizar as configurações para %s", OMS)));
    
    var settings = connection.getListenerSettings();
    return settings.getManualContainerFactory(factory);
  }

  @Bean(name = "oms-template")
  RabbitTemplate rabbitMQTemplate(
    @Qualifier("oms-connection") ConnectionFactory connection
  ) {
    return createTemplate(connection);
  }
}
