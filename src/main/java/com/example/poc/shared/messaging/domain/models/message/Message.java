package com.example.poc.shared.messaging.domain.models.message;

public interface Message<T> {
  long getDeathCount();
  long getCurrentAttempt();
  boolean alreadyReachedMaxOfAttempts();
  <E> E getHeader(String name);
  T getBody();
}
