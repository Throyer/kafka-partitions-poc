package com.github.throyer.rabbitmq.streams.listeners;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.throyer.rabbitmq.streams.models.Event;
import com.github.throyer.rabbitmq.streams.utils.JSON;
import com.rabbitmq.stream.Consumer;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.OffsetSpecification;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2) // Executa depois do producer ser inicializado
public class EventStreamListener implements CommandLineRunner {
  
  private final Environment environment;
  
  @Value("${rabbitmq.stream.name:events.stream}")
  private String streamName;
  
  @Value("${rabbitmq.stream.consumer.group:events-processor}")
  private String consumerGroup;
  
  private Consumer consumer;

  @Override
  public void run(String... args) throws Exception {
    log.info("Inicializando consumidor da Super Stream: {}", streamName);
    log.info("Consumer Group: {} (garante Single Active Consumer por partição)", consumerGroup);
    
    this.consumer = environment.consumerBuilder()
        .superStream(streamName)
        .name(consumerGroup) // Define o Consumer Group (Single Active Consumer)
        .singleActiveConsumer() // Garante que apenas um consumidor por partição está ativo
        .offset(OffsetSpecification.next()) // Começa a ler as novas mensagens
        .messageHandler((context, message) -> {
          try {
            // Deserializa a mensagem
            var event = JSON.deSerialize(message.getBodyAsBinary(), Event.class);

            log.info("Evento recebido: id={}, code={}, stream={}", 
                event.getId(), event.getCode(), context.stream());
            log.debug("Processando evento: {}", JSON.stringify(event));

            // TODO: Adicionar lógica de processamento aqui
            // - Verificar idempotência no MongoDB
            // - Processar evento
            // - Marcar como processado

          } catch (Exception e) {
            log.error("Erro ao processar evento", e);
          }
        })
        .build();
    
    log.info("Consumidor iniciado com sucesso. Aguardando eventos...");
  }
  
  @PreDestroy
  public void cleanup() {
    if (consumer != null) {
      log.info("Fechando consumidor");
      consumer.close();
    }
  }
}
