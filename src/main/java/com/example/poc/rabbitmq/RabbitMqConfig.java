package com.example.poc.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PEDIDOS = "pedidos";

    @Bean
    Queue pedidosQueue() {
        return new Queue(PEDIDOS, true);
    }

    @Bean
    DirectExchange pedidosExchange() {
        return new DirectExchange(PEDIDOS, true, false);
    }

    @Bean
    Binding pedidosBinding(Queue pedidosQueue, DirectExchange pedidosExchange) {
        return BindingBuilder.bind(pedidosQueue).to(pedidosExchange).with(PEDIDOS);
    }
}
