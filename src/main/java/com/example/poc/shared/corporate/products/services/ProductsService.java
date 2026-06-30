package com.example.poc.shared.corporate.products.services;

import com.example.poc.shared.corporate.products.clients.ProductsClient;
import com.example.poc.shared.corporate.products.domain.dto.ProductDTO;
import feign.FeignException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProductsService {
  private final ProductsClient client;

  public Optional<ProductDTO> getBySku(String sku) {
    try {
      return Optional.ofNullable(client.getBySku(sku).getBody());
    } catch (FeignException exception) {
      log.error(
        "failed to fetch product. sku: {}, status: {}",
        sku,
        exception.status(),
        exception
      );
      return Optional.empty();
    }
  }
}
