package com.github.throyer.rabbitmq.streams.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.streams.models.Event;
import com.github.throyer.rabbitmq.streams.utils.JSON;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Order(1) // Garante que o producer é inicializado primeiro
public class EventPublisherService implements CommandLineRunner {

  private final Environment environment;
  
  @Value("${rabbitmq.stream.name:events.stream}")
  private String streamName;
  
  private Producer producer;

  @Override
  public void run(String... args) throws Exception {
    log.info("Inicializando producer para Super Stream: {}", streamName);
    this.producer = environment
      .producerBuilder()
      .superStream(streamName)
      .routing(message -> {
        // Extrai o Event.id das propriedades da mensagem para usar como routing key
        // Isso garante que eventos com mesmo ID vão sempre para a mesma partição
        return message.getProperties().getMessageIdAsString();
      })
      .producerBuilder()
      .build();
    log.info("Producer inicializado com sucesso!");
  }

  @PreDestroy
  public void cleanup() {
    if (producer != null) {
      log.info("Fechando producer");
      producer.close();
    }
  }

  public void publishEvent(Event event) {
    log.info("Publicando evento na stream: id={}, code={}", event.getId(), event.getCode());

    var message = producer
      .messageBuilder()
      .properties()
        .messageId(event.getId()) // Event.id será usado como routing key
      .messageBuilder()
      .addData(JSON.serialize(event))
      .build();

    producer.send(message, (result) -> {
      if (result.isConfirmed()) {
        log.info("Evento publicado com sucesso: id={}", event.getId());
      } else {
        log.error("Falha ao publicar evento: id={}", event.getId());
      }
    });
  }
}
