package com.github.throyer.kafka.consumer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AfterSaleEvent {
  private String code;
  private LocalDateTime timestamp;
}
