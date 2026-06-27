package com.example.poc.shared.corporate.customers.clients;

import com.example.poc.configuration.http.rest.auth.HttpClientDefaultConfiguration;
import com.example.poc.shared.corporate.customers.domain.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
  name = "customers",
  url = "${corporate.customers-host}",
  configuration = HttpClientDefaultConfiguration.class
)
public interface CustomersClient {
  @GetMapping(
    value = "/customers/{customerId}",
    headers = {"Content-Type=application/json"}
  )
  ResponseEntity<CustomerDTO> getById(@PathVariable("customerId") String customerId);
}
