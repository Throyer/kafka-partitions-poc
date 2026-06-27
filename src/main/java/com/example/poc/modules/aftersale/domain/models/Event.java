package com.example.poc.modules.aftersale.domain.models;

import com.example.poc.shared.common.domain.models.Trigger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
  private String code;
  private String orderNumber;
  private Trigger trigger;
  private LocalDateTime timestamp;
}
