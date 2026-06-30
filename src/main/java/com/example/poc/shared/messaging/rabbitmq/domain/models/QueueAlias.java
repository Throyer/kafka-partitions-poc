package com.example.poc.shared.messaging.rabbitmq.domain.models;

import lombok.Getter;

@Getter
public enum QueueAlias {
  TRACKING_UPDATE_AFTERSALE("update-after-sale-queue"),
  OMS_ORDER_STATUS("order-status-queue");

  private final String description;

  QueueAlias(String description) {
    this.description = description;
  }
}
