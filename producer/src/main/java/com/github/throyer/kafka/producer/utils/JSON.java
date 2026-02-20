package com.github.throyer.kafka.producer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class JSON {
  private JSON() { }

  public static final ObjectMapper MAPPER = new ObjectMapper()
    .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(WRITE_DATES_AS_TIMESTAMPS, false)
    .configure(IGNORE_UNKNOWN, true)
    .findAndRegisterModules();

  public static <T> byte[] serialize(final T object) {
    try {
      return MAPPER.writeValueAsBytes(object);
    } catch (JsonProcessingException exception) {
      return null;
    }
  }

  public static <T> T deSerialize(byte[] value, Class<T> type) {
    try {
      return MAPPER.readValue(value, type);
    } catch (IOException exception) {
      return null;
    }
  }

  public static <T> String stringify(final T object) {
    try {
      return MAPPER.writer().writeValueAsString(object);
    } catch (JsonProcessingException exception) {
      return "";
    }
  }
}
