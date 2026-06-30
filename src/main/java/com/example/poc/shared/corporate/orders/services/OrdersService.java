package com.example.poc.shared.corporate.orders.services;

import com.example.poc.shared.corporate.orders.clients.OrdersClient;
import com.example.poc.shared.corporate.orders.domain.dto.OrderDTO;
import feign.FeignException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class OrdersService {
  private final OrdersClient client;

  public Optional<OrderDTO> getByOrderNumber(String orderNumber) {
    try {
      return Optional.ofNullable(client.getByOrderNumber(orderNumber).getBody());
    } catch (FeignException exception) {
      log.error(
        "failed to fetch order. orderNumber: {}, status: {}",
        orderNumber,
        exception.status(),
        exception
      );
      return Optional.empty();
    }
  }
}
