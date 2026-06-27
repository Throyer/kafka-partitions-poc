package com.example.poc.modules.aftersale.repositories;

import com.example.poc.modules.aftersale.domain.models.ReceivedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceivedEventRepository extends MongoRepository<ReceivedEvent, String> {}
