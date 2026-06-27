package com.example.poc.modules.timeline.services;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.timeline.domain.models.database.ReceivedEvent;
import com.example.poc.modules.timeline.repositories.ReceivedEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CreateReceivedEventService {
  private final ReceivedEventRepository repository;
  
  public ReceivedEvent create(Event event) {
    var received = new ReceivedEvent(
      null,
      event.getOrderNumber(),
      event,
      LocalDateTime.now()
    );
    
    repository.save(received);
    
    return received;
  }
}
