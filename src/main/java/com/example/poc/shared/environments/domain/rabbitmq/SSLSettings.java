package com.example.poc.shared.environments.domain.rabbitmq;

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
