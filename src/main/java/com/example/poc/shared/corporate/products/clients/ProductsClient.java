package com.example.poc.shared.corporate.products.clients;

import com.example.poc.configuration.http.rest.auth.HttpClientDefaultConfiguration;
import com.example.poc.shared.corporate.products.domain.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
  name = "products",
  url = "${corporate.products-host}",
  configuration = HttpClientDefaultConfiguration.class
)
public interface ProductsClient {
  @GetMapping(
    value = "/products/{sku}",
    headers = {"Content-Type=application/json"}
  )
  ResponseEntity<ProductDTO> getBySku(@PathVariable("sku") String sku);
}
