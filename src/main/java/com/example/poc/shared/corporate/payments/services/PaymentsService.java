package com.example.poc.shared.corporate.payments.services;

import com.example.poc.shared.corporate.payments.clients.PaymentsClient;
import com.example.poc.shared.corporate.payments.domain.dto.PaymentDTO;
import feign.FeignException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentsService {
  private final PaymentsClient client;

  public Optional<PaymentDTO> getByOrderNumber(String orderNumber) {
    try {
      return Optional.ofNullable(client.getByOrderNumber(orderNumber).getBody());
    } catch (FeignException exception) {
      log.error(
        "failed to fetch payment. orderNumber: {}, status: {}",
        orderNumber,
        exception.status(),
        exception
      );
      return Optional.empty();
    }
  }
}
