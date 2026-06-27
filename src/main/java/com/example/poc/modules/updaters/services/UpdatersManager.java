package com.example.poc.modules.updaters.services;

import com.example.poc.modules.aftersale.domain.models.Event;
import com.example.poc.modules.updaters.domain.models.AfterSaleUpdater;
import com.example.poc.modules.updaters.domain.models.EventParametersIml;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UpdatersManager {
  private final List<AfterSaleUpdater> updaters;
  
  public void update(Event event) {
    var parameters = new EventParametersIml(event);
    var eligible = updaters
      .stream()
      .filter(updater -> updater.eligible(parameters))
      .toList();
    
    for (var updater : eligible) {
      updater.update(parameters);
    }
  }
}
