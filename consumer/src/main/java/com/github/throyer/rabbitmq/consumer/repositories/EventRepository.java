package com.github.throyer.rabbitmq.consumer.repositories;

import com.github.throyer.rabbitmq.consumer.models.AfterSale;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventRepository extends MongoRepository<AfterSale, String> {
  Optional<AfterSale> findByOrderNumber(String orderNumber);  
}
