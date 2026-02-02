package com.github.throyer.rabbitmq.producer.controllers;

import com.github.throyer.rabbitmq.producer.services.AfterSaleUpdateProducer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.rabbitmq.producer.models.Event;

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
