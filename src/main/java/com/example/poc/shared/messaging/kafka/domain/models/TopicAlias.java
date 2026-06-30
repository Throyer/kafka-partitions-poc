package com.example.poc.shared.messaging.kafka.domain.models;

import lombok.Getter;

@Getter
public enum TopicAlias {
  TRACKING_UPDATE_AFTERSALE("update-after-sale-topic");

  private final String description;

  TopicAlias(String description) {
    this.description = description;
  }
}
