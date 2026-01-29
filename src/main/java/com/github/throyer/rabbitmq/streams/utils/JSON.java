package com.github.throyer.rabbitmq.streams.utils;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class JSON {
  private JSON() { }
  
  public static final ObjectMapper MAPPER = JsonMapper
    .builder()
    .build();

  public static <T> String stringify(final T object) {
    try {
      return MAPPER.writer().writeValueAsString(object);
    } catch (JacksonException exception) {
      return "";
    }
  }
}
