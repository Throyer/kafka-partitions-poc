package com.example.poc.shared.corporate.payments.clients;

import com.example.poc.configuration.http.rest.auth.HttpClientDefaultConfiguration;
import com.example.poc.shared.corporate.payments.domain.dto.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
  name = "payments",
  url = "${corporate.payments-host}",
  configuration = HttpClientDefaultConfiguration.class
)
public interface PaymentsClient {
  @GetMapping(
    value = "/payments/{orderNumber}",
    headers = {"Content-Type=application/json"}
  )
  ResponseEntity<PaymentDTO> getByOrderNumber(@PathVariable("orderNumber") String orderNumber);
}
