package com.example.poc.shared.messaging.domain.models.message;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.Map;

import org.springframework.amqp.core.MessageProperties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rabbitmq.client.Channel;

import lombok.Getter;

public class ChannelManager {
  static final String DEATH_COUNT_KEY_NAME = "count";

  @JsonIgnore
  private final Channel channel;

  @JsonIgnore
  private final MessageProperties properties;

  @Getter
  private final long maxRetryAttempts;

  private Boolean running = true;

  private ChannelManager(
    Channel channel,
    MessageProperties properties,
    int maxRetryAttempts
  ) {
    this.channel = channel;
    this.properties = properties;
    this.maxRetryAttempts = maxRetryAttempts;
  }

  public static ChannelManager create(
    Channel channel,
    MessageProperties properties,
    int maxRetryAttempts
  ) {
    return new ChannelManager(channel, properties, maxRetryAttempts);
  }

  public Boolean isRunning() {
    return running;
  }

  public void doAck() throws IOException {
    running = false;
    channel.basicAck(properties.getDeliveryTag(), false);
  }

  public void doReject() throws IOException {
    running = false;
    channel.basicReject(properties.getDeliveryTag(), false);
  }

  public long getCurrentAttempt() {
    return getDeathCount() + 1;
  }

  public long getDeathCount() {
    if (isNull(properties.getXDeathHeader())) {
      return 0L;
    }

    var deaths = properties.getXDeathHeader();
    var expires = deaths.stream().filter(this::isDeathHeader)
      .findFirst();

    if (expires.isEmpty()) {
      return 0L;
    }

    var header = expires.get();

    if (!header.isEmpty()) {
      return (Long) header.get(DEATH_COUNT_KEY_NAME);
    }

    return 0L;
  }

  public Boolean alreadyReachedMaxOfAttempts() {
    var attempts = getCurrentAttempt();
    return attempts >= maxRetryAttempts;
  }

  private Boolean isDeathHeader(Map<String, ?> header) {
    if (!header.containsKey("reason")) {
      return false;
    }

    String value = (String) header.get("reason");

    if (isNull(value)) {
      return false;
    }

    return value.equalsIgnoreCase("rejected");
  }
}
