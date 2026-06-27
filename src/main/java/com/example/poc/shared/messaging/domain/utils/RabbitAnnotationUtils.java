package com.example.poc.shared.messaging.domain.utils;

import com.example.poc.shared.messaging.domain.annotations.RabbitConnection;
import com.example.poc.shared.messaging.domain.models.Connection;
import com.example.poc.shared.messaging.domain.models.RetryListener;

public class RabbitAnnotationUtils {
  public static String getQueue(RetryListener<?> listener) {
    var type = listener.getClass();
    var annotation = type.getAnnotation(RabbitConnection.class);
    return annotation.queue();
  }

  public static Connection getConnection(RetryListener<?> listener) {
    var type = listener.getClass();
    var annotation = type.getAnnotation(RabbitConnection.class);
    return annotation.connection();
  }
}
