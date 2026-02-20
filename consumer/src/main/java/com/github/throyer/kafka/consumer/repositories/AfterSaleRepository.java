package com.github.throyer.kafka.consumer.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.github.throyer.kafka.consumer.models.AfterSale;

import java.util.Optional;

public interface AfterSaleRepository extends MongoRepository<AfterSale, String> {
  Optional<AfterSale> findByOrderNumber(@Param("orderNumber") String orderNumber);  
}
