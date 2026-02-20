package com.github.throyer.kafka.producer.models;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {
  @NotEmpty
  private String orderNumber;
  
  @NotEmpty
  private String code;
}
