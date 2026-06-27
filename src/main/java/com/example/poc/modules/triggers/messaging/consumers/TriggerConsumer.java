package com.example.poc.modules.triggers.messaging.consumers;

import com.example.poc.modules.triggers.domain.models.ExternalTriggerEvent;
import com.example.poc.modules.triggers.services.ProcessExternalEventService;
import com.example.poc.shared.messaging.domain.models.message.Message;
import com.example.poc.shared.messaging.domain.models.message.RetryListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public abstract class TriggerConsumer<T extends ExternalTriggerEvent> implements RetryListener<T> {
  protected final ProcessExternalEventService service;
  
  @Override
  public void onMessage(Message<T> message) {
    var event = message.getBody();
    service.process(event);  
  }
}
