package com.example.poc.shared.corporate.orders.clients;

import com.example.poc.configuration.http.rest.auth.HttpClientDefaultConfiguration;
import com.example.poc.shared.corporate.orders.domain.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
  name = "orders",
  url = "${corporate.orders-host}",
  configuration = HttpClientDefaultConfiguration.class
)
public interface OrdersClient {
  @GetMapping(
    value = "/orders/{orderNumber}",
    headers = {"Content-Type=application/json"}
  )
  ResponseEntity<OrderDTO> getByOrderNumber(@PathVariable("orderNumber") String orderNumber);
}
