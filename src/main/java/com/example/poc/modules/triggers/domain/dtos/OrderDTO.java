package com.example.poc.modules.triggers.domain.dtos;

import com.example.poc.modules.triggers.domain.models.ExternalTriggerEvent;
import com.example.poc.shared.common.domain.models.Trigger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.poc.shared.common.domain.constants.Constants.CUSTOM_DATE_TIME_PATTERN;
import static com.example.poc.shared.common.domain.models.Trigger.OMS;
import static java.time.LocalDateTime.parse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements ExternalTriggerEvent {
  private Long orderNumber;
  private Status status;
  private List<HistoricStatus> historicStatus;
  private List<Item> items;
  
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Status {
    @NotNull
    private Integer id;
    private String name;
  }
  
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Item {
    private Long productId;
    private BigDecimal totalItem;
    private Integer quantity;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class HistoricStatus {
    private String creationDate;
    private Status status;
    
    @JsonIgnore
    public boolean is(String code) {
      return status.id.toString().equals(code);
    }
  }
  
  @Override
  public Trigger trigger() {
    return OMS;
  }

  @Override
  public String orderNumber() {
    return Optional.of(orderNumber)
      .map(Objects::toString)
      .orElse("");
  }

  @Override
  public String statusCode() {
    return Optional.of(status)
      .map(Status::getId)
      .map(Objects::toString)
      .orElse("");
  }

  @Override
  public LocalDateTime timestamp() {
    return historicStatus
      .stream()
      .filter(status -> status.is(statusCode()))
      .findFirst()
      .map(HistoricStatus::getCreationDate)
      .map(date -> parse(date, CUSTOM_DATE_TIME_PATTERN))
      .orElseGet(LocalDateTime::now);
  }
}
