package com.example.poc.shared.exceptions.domain;

import lombok.Getter;

@Getter
public class JsonParseException extends RuntimeException {
  private final String json;
  public JsonParseException(String json, Throwable cause) {
    super(cause);
    this.json = json;
  }
}
