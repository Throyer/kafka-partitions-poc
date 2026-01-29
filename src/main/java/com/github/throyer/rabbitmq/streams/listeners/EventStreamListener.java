package com.github.throyer.rabbitmq.streams.listeners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.throyer.rabbitmq.streams.models.Event;
import com.github.throyer.rabbitmq.streams.utils.JSON;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class EventStreamListener implements CommandLineRunner {
  private final Environment environment;

  @Override
  public void run(String... args) throws Exception {
    log.info("@PostConstruct");
    environment.consumerBuilder()
        .superStream("events.stream")
        .name("events-processor") // Define o Consumer Group (importante!)
        .offset(OffsetSpecification.next()) // Começa a ler as novas
        .messageHandler((context, message) -> {
          try {
            // Transforma os bytes em Pedido
            var event = JSON.deSerialize(message.getBodyAsBinary(), Event.class);

            log.info("Evento recebido: id={}, code={}", event.getId(), event.getCode());
            log.debug("Processando evento: {}", JSON.stringify(event));

            // Lógica do MongoDB...

          } catch (Exception e) {
            e.printStackTrace();
          }
        })
        .build();
  }
}
