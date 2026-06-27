package com.example.poc.modules.updaters.domain.updaters;

import org.springframework.stereotype.Component;
import com.example.poc.modules.aftersale.services.CreateEmptyAfterSaleService;
import com.example.poc.modules.updaters.domain.models.AfterSaleUpdater;
import com.example.poc.modules.updaters.domain.models.EventParameters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class CreateAfterSale implements AfterSaleUpdater {
  private CreateEmptyAfterSaleService emptyAfterSaleService;
  
  @Override
  public boolean eligible(EventParameters parameters) {
    return parameters.hasStatusCode("1011");
  }

  @Override
  public void update(EventParameters parameters) {
    log.debug("criando pedido com status de pedido criado.");
    var orderNumber = parameters.orderNumber();
    emptyAfterSaleService.create(orderNumber);
  }
}
