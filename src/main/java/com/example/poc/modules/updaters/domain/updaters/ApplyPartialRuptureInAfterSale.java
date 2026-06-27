package com.example.poc.modules.updaters.domain.updaters;

import org.springframework.stereotype.Service;
import com.example.poc.modules.updaters.domain.models.AfterSaleUpdater;
import com.example.poc.modules.updaters.domain.models.EventParameters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ApplyPartialRuptureInAfterSale implements AfterSaleUpdater {  
  @Override
  public boolean eligible(EventParameters parameters) {
    return parameters.hasStatusCode("145");
  }

  @Override
  public void update(EventParameters parameters) {
    log.debug("fazendo alteração de ruptura parcial");
  }
}
