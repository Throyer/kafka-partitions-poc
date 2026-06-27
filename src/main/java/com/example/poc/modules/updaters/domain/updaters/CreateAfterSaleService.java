package com.example.poc.modules.updaters.domain.updaters;

import com.example.poc.modules.updaters.domain.models.AfterSaleUpdater;
import com.example.poc.modules.updaters.domain.models.EventParameters;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateAfterSaleService implements AfterSaleUpdater {
  @Override
  public boolean eligible(EventParameters parameters) {
    return false;
  }

  @Override
  public void update(EventParameters parameters) {

  }
}
