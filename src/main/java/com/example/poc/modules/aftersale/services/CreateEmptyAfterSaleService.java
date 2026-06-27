package com.example.poc.modules.aftersale.services;

import com.example.poc.modules.aftersale.domain.models.AfterSale;
import com.example.poc.modules.aftersale.domain.models.Customer;
import com.example.poc.modules.aftersale.domain.models.Item;
import com.example.poc.modules.aftersale.domain.models.Payment;
import com.example.poc.modules.aftersale.repositories.AfterSaleRepository;
import com.example.poc.shared.corporate.customers.services.CustomersService;
import com.example.poc.shared.corporate.orders.services.OrdersService;
import com.example.poc.shared.corporate.payments.services.PaymentsService;
import com.example.poc.shared.corporate.products.services.ProductsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class CreateEmptyAfterSaleService {
  private final OrdersService ordersService;
  private final CustomersService customersService;
  private final PaymentsService paymentsService;
  private final ProductsService productsService;
  private final AfterSaleRepository afterSaleRepository;
  
  public AfterSale create(String orderNumber) {
    var order = ordersService.getByOrderNumber(orderNumber)
      .orElseThrow(() -> new RuntimeException("pedido não encontrado"));

    var customer = customersService.getById(order.getCustomerId())
      .orElseThrow(() -> new RuntimeException("customer não encontrado"));

    var payment = paymentsService.getByOrderNumber(orderNumber)
      .orElseThrow(() -> new RuntimeException("dados de pagamento não encontrado"));

    var items = new ArrayList<Item>();

    for (var itemInOrder : order.getItems()) {
      var sku = itemInOrder.getProductId().toString();
      var product = productsService.getBySku(sku)
        .orElse(null);

      if (isNull(product)) {
        continue;
      }

      items.add(new Item(
        sku,
        product.getName(),
        product.getImageUrl(),
        product.getDescription()
      ));
    }

    var afterSale = new AfterSale(
      null,
      orderNumber,
      new Customer(
        customer.getId(),
        customer.getName(),
        customer.getEmail()
      ),
      new Payment(
        payment.getId(),
        payment.getPaymentType(),
        payment.getTotalValue()
      ),
      items
    );

    afterSaleRepository.save(afterSale);
    
    return afterSale;
  }
}
