package com.example.poc.modules.updaters.domain.models;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.common.domain.models.Trigger;
import lombok.AllArgsConstructor;

import java.util.stream.Stream;

@AllArgsConstructor
public class EventParametersIml implements EventParameters {
  private final Event event;

  @Override
  public String orderNumber() {
    return event.getOrderNumber();
  }

  @Override
  public boolean hasTrigger(Trigger... candidates) {
    if (candidates.length == 0) {
      return false;
    }
    
    return Stream.of(candidates)
      .anyMatch(trigger -> event.getTrigger().equals(trigger));
  }

  @Override
  public boolean hasStatusCode(String... candidates) {
    if (candidates.length == 0) {
      return false;
    }

    return Stream.of(candidates)
      .anyMatch(code -> event.getStatusCode().equals(code));
  }
}
