package com.example.poc.shared.common.domain.constants;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Constants {
  public static final ZoneId TIMEZONE_AMERICA_SAO_PAULO = ZoneId.of("America/Sao_Paulo");
  public static final ZoneId TIMEZONE_UTC = ZoneId.of("UTC");
  public static final DateTimeFormatter CUSTOM_DATE_TIME_PATTERN = ofPattern("yyyy-MM-dd HH:mm:ss");
}
