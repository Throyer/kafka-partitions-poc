package com.example.poc.modules.updaters.domain.updaters;

import org.springframework.stereotype.Service;
import com.example.poc.modules.updaters.domain.models.AfterSaleUpdater;
import com.example.poc.modules.updaters.domain.models.EventParameters;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PartialRuptureService implements AfterSaleUpdater {
  @Override
  public boolean eligible(EventParameters parameters) {
    return false;
  }

  @Override
  public void update(EventParameters parameters) {

  }
}
