package com.example.poc.modules.triggers.controllers;

import static org.springframework.http.HttpStatus.ACCEPTED;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.poc.modules.triggers.domain.dtos.OrderDTO;
import com.example.poc.modules.triggers.messaging.producers.OrderStatusProducer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/triggers")
public class PublishOrderController {
  private final OrderStatusProducer producer;
  
  @PostMapping("/publish/order")
  @ResponseStatus(ACCEPTED)
  public void publish(@RequestBody OrderDTO order) {
    producer.publish(order);
  }
}
