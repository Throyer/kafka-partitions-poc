package com.example.poc.shared.common.domain.utils;

import static java.lang.String.format;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

public class TimeUtils {
  public static String elapsedSeconds(long startTimeInNanoseconds, long endTimeInNanoseconds) {
    var elapsedTime = (endTimeInNanoseconds - startTimeInNanoseconds) / 1e+9;
    return format(new Locale("pt-BR"), "%.1fs", elapsedTime);
  }

  public static String elapsedTime(Instant start, Instant end) {
    var duration = Duration.between(start, end);
    var seconds = duration.toSeconds();
    if (seconds < 1) {
      var millis = duration.toMillis();
      return format("%sms", millis);
    }
    return format("%ss", seconds);
  }
}

