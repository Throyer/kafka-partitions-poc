package com.example.poc.shared.messaging.rabbitmq.domain.models.message;

import static java.time.LocalDateTime.now;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FailImpl<T> implements Fail<T> {
  private final Throwable cause;
  private final List<Error> errors;
  private final LocalDateTime createdAt;
  private final T body;

  public FailImpl(
    Throwable exception,
    T body
  ) {
    this.cause = exception;
    this.errors = List.of(new Error(exception.getMessage()));
    this.createdAt = now();
    this.body = body;
  }

  public FailImpl(
    List<Error> errors,
    T body
  ) {
    this.cause = null;
    this.errors = errors;
    this.createdAt = now();
    this.body = body;
  }
  
  @Override
  public Throwable cause() {
    return this.cause;
  }

  @Override
  public List<Error> errors() {
    return errors;
  }

  @Override
  public LocalDateTime createdAt() {
    return createdAt;
  }

  @Override
  public Optional<T> getBody() {
    return Optional.ofNullable(body);
  }
}
