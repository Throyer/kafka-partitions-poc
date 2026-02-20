package com.github.throyer.kafka.producer.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.kafka.producer.models.Event;
import com.github.throyer.kafka.producer.services.AfterSaleUpdateProducer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

  private final AfterSaleUpdateProducer producer;

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void publishEvent(@RequestBody @Valid Event event) {
    producer.publish(event);
  }
}
