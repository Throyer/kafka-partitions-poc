package com.example.poc;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

@EnableRabbit
@SpringBootApplication(
  exclude = {RabbitAutoConfiguration.class}
)
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
