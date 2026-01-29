package com.github.throyer.rabbitmq.streams.controllers;

import com.github.throyer.rabbitmq.streams.models.Event;
import com.github.throyer.rabbitmq.streams.services.EventPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

  private final EventPublisherService eventPublisherService;

  @PostMapping
  public ResponseEntity<Event> publishEvent(@RequestBody Event event) {
    eventPublisherService.publishEvent(event);
    return ResponseEntity.status(HttpStatus.CREATED).body(event);
  }
}
