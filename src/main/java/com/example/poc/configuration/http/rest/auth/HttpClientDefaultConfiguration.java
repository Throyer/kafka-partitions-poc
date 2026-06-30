package com.example.poc.configuration.http.rest.auth;

import feign.Logger.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientDefaultConfiguration {
  @Bean
  Level logging(@Value("${feign.logging.level:NONE}") Level level) {
    return level;
  }
}
