package com.example.poc.shared.messaging.rabbitmq.services;

import com.example.poc.shared.environments.domain.ConnectionsProperties;
import com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection;
import com.example.poc.shared.messaging.rabbitmq.domain.models.connection.ConnectionSettings;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection.*;

@Service
public class RabbitMqConnectionManager {
  private final ConnectionsProperties properties;
  private final Map<Connection, ConnectionFactory> factories;
  
  public RabbitMqConnectionManager(
    ConnectionsProperties properties,
    
    @Qualifier("tracking-connection")
    ConnectionFactory tracking,

    @Qualifier("oms-connection")
    ConnectionFactory oms,

    @Qualifier("tms-connection")
    ConnectionFactory tms
  ) {
    this.properties = properties;
    this.factories = Map.of(
      TRACKING, tracking,
      OMS, oms,
      TMS, tms
    );
  }
  
  public Optional<ConnectionFactory> getFactoryByConnection(Connection name) {
    if (!factories.containsKey(name)) {
      return Optional.empty();
    }
    
    var connection = factories.get(name);    
    return Optional.of(connection);
  }
  
  public Optional<ConnectionSettings> getSettingsByConnection(Connection connection) {
    return properties.getByConnection(connection);
  }
}
