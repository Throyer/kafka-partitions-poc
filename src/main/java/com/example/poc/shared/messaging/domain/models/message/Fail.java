package com.example.poc.shared.messaging.domain.models.message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface Fail<T> {
  Throwable cause();
  List<Error> errors();
  LocalDateTime createdAt();
  Optional<T> getBody();
}
