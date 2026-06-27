package com.example.poc.modules.aftersale.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.poc.modules.aftersale.domain.models.AfterSaleWithTimeline;
import com.example.poc.modules.aftersale.services.AfterSaleService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/aftersale")
public class AfterSaleController {
  private final AfterSaleService service;

  @GetMapping("/{orderNumber}")
  public AfterSaleWithTimeline show(@PathVariable String orderNumber) {
    return service.show(orderNumber);
  }
}
