package com.github.throyer.rabbitmq.streams.controllers;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.rabbitmq.streams.models.Event;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

  private final StreamBridge bridge;

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void publishEvent(@RequestBody Event event) {
    bridge.send("pedidos-out-0", event);
  }
}
