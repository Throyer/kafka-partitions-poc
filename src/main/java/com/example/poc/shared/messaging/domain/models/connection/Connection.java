package com.example.poc.shared.messaging.domain.models.connection;

import lombok.Getter;

@Getter
public enum Connection {
  OMS("oms/order"),
  TMS("TMS"),
  TRACKING("squad-tracking/aftersale");

  private final String description;

  Connection(String description) {
    this.description = description;
  }
}
