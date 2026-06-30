package com.example.poc.modules.aftersale.repositories;

import com.example.poc.modules.aftersale.domain.models.AfterSale;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AfterSaleRepository extends MongoRepository<AfterSale, String> {
  boolean existsByOrderNumber(String orderNumber);
  Optional<AfterSale> findByOrderNumber(String orderNumber);
}
