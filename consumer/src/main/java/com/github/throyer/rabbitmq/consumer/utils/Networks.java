package com.github.throyer.rabbitmq.consumer.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Networks {
  public static String hostname() {
    try {
      return InetAddress
        .getLocalHost()
        .getHostName();
    } catch (UnknownHostException exception) {
      return "";
    }
  }
}
