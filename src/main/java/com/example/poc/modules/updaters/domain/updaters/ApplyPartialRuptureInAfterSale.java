package com.example.poc.modules.updaters.domain.updaters;

import static com.example.poc.modules.aftersale.domain.models.rupture.RuptureType.REMOVED;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.poc.modules.aftersale.domain.models.rupture.Rupture;
import com.example.poc.modules.aftersale.services.AfterSaleService;
import com.example.poc.modules.updaters.domain.models.AfterSaleUpdater;
import com.example.poc.modules.updaters.domain.models.EventParameters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ApplyPartialRuptureInAfterSale implements AfterSaleUpdater {
  private final AfterSaleService service;
  
  @Override
  public boolean eligible(EventParameters parameters) {
    return parameters.hasStatusCode("145");
  }

  @Override
  public void update(EventParameters parameters) {
    log.debug("fazendo alteração de ruptura parcial");
    var orderNumber = parameters.orderNumber();
    var afterSale = service.find(orderNumber)
      .orElseThrow(() -> new RuntimeException("não foi possível localizar o pedido"));
    
    var length = afterSale
      .getItems()
      .size(); 
    
    if (length > 1) {
      var item = afterSale
        .getItems()
        .get(0);
      
      var rupture = new Rupture(REMOVED, item);
      var items = afterSale
        .getItems()
        .stream()
        .skip(1)
        .toList();
      
      afterSale.setRupture(List.of(rupture));
      afterSale.setItems(items);
      
      service.save(afterSale);
    }
  }
}
