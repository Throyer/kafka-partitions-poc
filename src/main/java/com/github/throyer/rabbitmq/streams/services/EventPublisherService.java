package com.github.throyer.rabbitmq.streams.services;

import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.streams.models.Event;
import com.github.throyer.rabbitmq.streams.utils.JSON;
import com.rabbitmq.stream.Environment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisherService {

  private final Environment environment;

  public void publishEvent(Event event) {
    log.info("Publicando evento na stream: id={}, code={}", event.getId(), event.getCode());
    var producer = environment
      .producerBuilder()
      .superStream("events.stream")
      .build();

    var message = producer
      .messageBuilder()
      .properties()
        .messageId(event.getId())
      .messageBuilder()
      .addData(JSON.serialize(event))
      .build();

    producer.send(message, (result) -> {
      log.info("event: {} sent: [{}]", JSON.stringify(event), result.isConfirmed());
    });
  }
}
