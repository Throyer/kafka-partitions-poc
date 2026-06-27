package com.example.poc.shared.messaging.services;

import com.example.poc.shared.messaging.domain.models.QueueAlias;
import com.example.poc.shared.messaging.domain.models.settings.QueueSettings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QueueSettingsManager {
  private final List<QueueSettings> settings;
  
  public Optional<QueueSettings> getByAlias(QueueAlias alias) {
    return settings
      .stream()
      .filter(queue -> queue.hasAlias(alias))
      .findFirst();
  }
}
