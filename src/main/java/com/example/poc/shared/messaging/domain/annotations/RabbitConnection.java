package com.example.poc.shared.messaging.domain.annotations;

import com.example.poc.shared.messaging.domain.models.connection.Connection;
import com.example.poc.shared.messaging.domain.models.QueueAlias;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RUNTIME)
public @interface RabbitConnection {
  Connection connection();
  QueueAlias queue();
}
