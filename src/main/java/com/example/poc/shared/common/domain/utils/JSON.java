package com.example.poc.shared.common.domain.utils;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import org.springframework.amqp.core.Message;
import com.example.poc.shared.exceptions.domain.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {
  private JSON() { }

  public static final ObjectMapper MAPPER = new ObjectMapper()
    .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(WRITE_DATES_AS_TIMESTAMPS, false)
    .configure(IGNORE_UNKNOWN, true)
    .findAndRegisterModules();

  public static <T> String stringify(final T object) {
    try {
      return MAPPER.writer().writeValueAsString(object);
    } catch (JsonProcessingException exception) {
      return "";
    }
  }

  public static <T> T parse(String json, Class<T> type) {
    try {
      return MAPPER.readValue(json, type);
    } catch (Exception exception) {
      throw new JsonParseException(json, exception);
    }
  }

  public static <T> T parse(String json, TypeReference<T> type) {
    try {
      return MAPPER.readValue(json, type);
    } catch (Exception exception) {
      throw new JsonParseException(json, exception);
    }
  }

  public static Message setContentTypeJson(Message message) {
    message.getMessageProperties().setContentType("json");
    return message;
  }
}
