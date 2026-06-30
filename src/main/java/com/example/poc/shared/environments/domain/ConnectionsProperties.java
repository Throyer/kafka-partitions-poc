package com.example.poc.shared.environments.domain;

import com.example.poc.shared.messaging.rabbitmq.domain.models.connection.Connection;
import com.example.poc.shared.messaging.rabbitmq.domain.models.connection.ConnectionSettings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rabbitmq.settings")
public class ConnectionsProperties {
  private Set<ConnectionSettings> connections;

  public Optional<ConnectionSettings> getByConnection(Connection name) {
    return connections
      .stream()
      .filter(connection -> connection.getName().equals(name))
      .findFirst();
  }
}