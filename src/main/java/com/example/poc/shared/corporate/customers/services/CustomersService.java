package com.example.poc.shared.corporate.customers.services;

import com.example.poc.shared.corporate.customers.clients.CustomersClient;
import com.example.poc.shared.corporate.customers.domain.dto.CustomerDTO;
import feign.FeignException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CustomersService {
  private final CustomersClient client;

  public Optional<CustomerDTO> getById(String customerId) {
    try {
      return Optional.ofNullable(client.getById(customerId).getBody());
    } catch (FeignException exception) {
      log.error(
        "failed to fetch customer. customerId: {}, status: {}",
        customerId,
        exception.status(),
        exception
      );
      return Optional.empty();
    }
  }
}
