package com.example.poc.shared.corporate.orders.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
  private Long orderNumber;
  private Status status;
  private List<HistoricStatus> historicStatus;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Status {
    private Integer id;
    private String name;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class HistoricStatus {
    private String creationDate;
    private Status status;
  }
}
