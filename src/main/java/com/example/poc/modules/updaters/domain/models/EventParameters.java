package com.example.poc.modules.updaters.domain.models;

import com.example.poc.shared.common.domain.models.Trigger;

public interface EventParameters {
  boolean hasTrigger(Trigger... candidates);
  boolean hasStatusCode(String... candidates);
}
