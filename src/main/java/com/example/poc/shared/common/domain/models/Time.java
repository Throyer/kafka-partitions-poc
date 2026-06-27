package com.example.poc.shared.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
public class Time {
  private final Integer amount;
  private final ChronoUnit unit;

  public static Time format(String value) {
    var split = value.split(":");
    var amount = Integer.parseInt(split[0]);
    var unit = ChronoUnit.valueOf(split[1]);
    return new Time(amount, unit);
  }

  public Duration toDuration() {
    return Duration.of(amount, unit);
  }
  
  public long toMillis() {
    return toDuration()
      .toMillis();
  }

  public static Time of(Integer amount, ChronoUnit unit) {
    return new Time(amount, unit);
  }
}
