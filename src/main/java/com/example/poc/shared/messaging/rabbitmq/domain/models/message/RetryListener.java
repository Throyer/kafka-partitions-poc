package com.example.poc.shared.messaging.rabbitmq.domain.models.message;

import com.example.poc.shared.common.domain.utils.JSON;
import com.example.poc.shared.exceptions.domain.DisposableMessageException;

import java.lang.reflect.ParameterizedType;

public interface RetryListener <T> {
  default void preValidation(T body) { }

  void onMessage(Message<T> message);

  default Boolean canAccept(Message<T> message) { return true; }

  default void onMaxRetryAttempts(Fail<T> fail) { }

  default void onValidationError(Fail<T> fail) { }

  default void onDisposeMessage(DisposableMessageException exception, Message<T> message) { }

  @SuppressWarnings("unchecked")
  default T parse(String message) {
    var type = (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0])
      .getActualTypeArguments()[0];
    return JSON.parse(message, type);
  }
}
