package com.example.poc.modules.timeline.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreviouslyStatus {
  private String code;
  private LocalDateTime timestamp;
}
