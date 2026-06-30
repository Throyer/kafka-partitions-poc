package com.example.poc.modules.timeline.services;

import com.example.poc.modules.timeline.domain.models.database.ReceivedEvent;
import com.example.poc.modules.timeline.repositories.ReceivedEventRepository;
import com.example.poc.modules.timeline.domain.models.CurrentStatus;
import com.example.poc.modules.timeline.domain.models.PreviouslyStatus;
import com.example.poc.modules.timeline.domain.models.Timeline;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ComposeTimelineService {
  private final ReceivedEventRepository repository;
  
  public Timeline compose(String orderNumber) {
    var events = repository.findByOrderNumber(orderNumber);
    
    var current = events
      .stream()
      .findFirst()
      .map(ReceivedEvent::getEvent)
      .orElse(null);

    var history = events
      .stream()
      .map(ReceivedEvent::getEvent)
      .skip(1)
      .map(event -> new PreviouslyStatus(
        event.getStatusCode(),
        event.getTimestamp()
      ))
      .toList();

    return new Timeline(
      new CurrentStatus(
        current.getStatusCode(),
        current.getTimestamp()
      ),
      history
    );
  }
}
