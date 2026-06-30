package com.example.poc.modules.timeline.repositories;

import com.example.poc.modules.timeline.domain.models.database.ReceivedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReceivedEventRepository extends MongoRepository<ReceivedEvent, String> {
  List<ReceivedEvent> findByOrderNumber(String orderNumber);
}
