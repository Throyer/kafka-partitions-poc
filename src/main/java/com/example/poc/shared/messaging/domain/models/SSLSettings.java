package com.example.poc.shared.messaging.domain.models;

import static lombok.AccessLevel.NONE;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSLSettings {
  @Getter(NONE)
  private Boolean enabled;
  private String algorithm;

  public Boolean isEnabled() {
    return enabled;
  }
}
