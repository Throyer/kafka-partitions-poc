package com.example.poc.modules.triggers.domain.models;

import com.example.poc.shared.common.domain.models.Trigger;

import java.time.LocalDateTime;

public interface ExternalTriggerEvent {
  Trigger trigger();
  String orderNumber();
  String statusCode();
  LocalDateTime timestamp();
}
