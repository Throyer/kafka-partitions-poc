package com.example.poc.shared.common.domain.utils;

import static java.util.Objects.isNull;

public class ExceptionUtils {
  public static String extractMessage(Throwable throwable) {
    if (isNull(throwable.getCause())) {
      return throwable.getMessage();
    }
    return throwable.getCause().getMessage();
  }
}