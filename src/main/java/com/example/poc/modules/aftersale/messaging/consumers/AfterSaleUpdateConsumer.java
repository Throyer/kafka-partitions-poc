package com.example.poc.modules.aftersale.messaging.consumers;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.shared.messaging.domain.models.Message;
import com.example.poc.shared.messaging.domain.models.RetryListener;

public class AfterSaleUpdateConsumer implements RetryListener<Event> {
  @Override
  public void onMessage(Message<Event> message) {
    
  }
}
