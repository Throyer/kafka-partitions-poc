package com.github.throyer.rabbitmq.producer.utils;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class JSON {
  private JSON() { }
  
  public static final ObjectMapper MAPPER = JsonMapper
    .builder()
    .build();

  public static <T> byte[] serialize(final T object) {
    return MAPPER.writeValueAsBytes(object);
  }

  public static <T> T deSerialize(byte[] value, Class<T> type) {
    try {
      return MAPPER.readValue(value, type);
    } catch (JacksonException exception) {
      return null;
    }
  }

  public static <T> String stringify(final T object) {
    try {
      return MAPPER.writer().writeValueAsString(object);
    } catch (JacksonException exception) {
      return "";
    }
  }
}
