package com.example.poc.modules.aftersale.domain.models;

import com.example.poc.shared.common.domain.models.Trigger;
import com.example.poc.shared.corporate.orders.domain.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.example.poc.shared.common.domain.constants.Constants.CUSTOM_DATE_TIME_PATTERN;
import static com.example.poc.shared.common.domain.models.Trigger.TRACKING;
import static java.time.LocalDateTime.parse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
  private String orderNumber;
  private String statusCode;
  private Trigger trigger;
  private LocalDateTime timestamp;
  
  public boolean is(String code) {
    return this.statusCode.equalsIgnoreCase(code);
  }

  public Event(String orderNumber, OrderDTO.HistoricStatus status) {
    this.orderNumber = orderNumber;
    this.statusCode = status.getStatus().getId().toString();
    this.trigger = TRACKING;
    this.timestamp = parse(status.getCreationDate(), CUSTOM_DATE_TIME_PATTERN);
  }
}
