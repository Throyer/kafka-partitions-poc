package com.github.throyer.kafka.consumer;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
  @GetMapping
  public Map<String, Object> index() {
    return Map.of("running", true);
  }
}
