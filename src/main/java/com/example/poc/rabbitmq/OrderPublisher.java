package com.example.poc.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    public OrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PEDIDOS_EXCHANGE,
                "",
                order,
                message -> {
                    message.getMessageProperties().setHeader(RabbitMqConfig.HASH_HEADER, order.orderNumber());
                    return message;
                });
    }
}
