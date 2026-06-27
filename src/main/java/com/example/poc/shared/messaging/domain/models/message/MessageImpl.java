package com.example.poc.shared.messaging.domain.models.message;

import org.springframework.amqp.core.MessageProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageImpl<T> implements Message<T> {
  private final T body;
  private final ChannelManager manager;
  private final MessageProperties properties;

  @Override
  public long getDeathCount() {
    return manager.getDeathCount();
  }

  @Override
  public long getCurrentAttempt() {
    return manager.getCurrentAttempt();
  }

  @Override
  public boolean alreadyReachedMaxOfAttempts() {
    return manager.alreadyReachedMaxOfAttempts();
  }

  @Override
  public <E> E getHeader(String name) {
    return this.properties.getHeader(name);
  }

  @Override
  public T getBody() {
    return body;
  }
}
