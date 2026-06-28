import { faker } from "@faker-js/faker";
import { getOrCreate, seedFromKey } from "./cache";
import { formatDateTime } from "./date-utils";
import { ORDER_STATUSES } from "./order-statuses";
import type { Customer, Order, OrderItem, Payment, Product } from "./types";

const customersCache = new Map<string, Customer>();
const ordersCache = new Map<string, Order>();
const paymentsCache = new Map<string, Payment>();
const productsCache = new Map<string, Product>();

const PAYMENT_TYPES = ["CREDIT_CARD", "DEBIT_CARD", "PIX", "BOLETO", "VOUCHER"];

function withSeed<T>(key: string, factory: () => T): T {
  faker.seed(seedFromKey(key));
  const value = factory();
  faker.seed();
  return value;
}

function buildOrderItems(): OrderItem[] {
  return Array.from({ length: faker.number.int({ min: 1, max: 5 }) }, () => {
    const quantity = faker.number.int({ min: 1, max: 10 });
    const unitPrice = Number(faker.commerce.price({ min: 5, max: 500, dec: 2 }));

    return {
      productId: faker.number.int({ min: 1000, max: 999999 }),
      quantity,
      totalItem: Number((unitPrice * quantity).toFixed(2)),
    };
  });
}

export function getCustomer(customerId: string): Customer {
  return getOrCreate(customersCache, customerId, () =>
    withSeed(`customer:${customerId}`, () => ({
      id: customerId,
      name: faker.person.fullName(),
      email: faker.internet.email(),
    })),
  );
}

export function getOrder(orderNumber: string): Order {
  return getOrCreate(ordersCache, orderNumber, () =>
    withSeed(`order:${orderNumber}`, () => {
      const parsedOrderNumber = Number(orderNumber);
      const currentStatusIndex = faker.number.int({ min: 0, max: ORDER_STATUSES.length - 1 });
      const currentStatus = ORDER_STATUSES[currentStatusIndex];
      const historicStatus = ORDER_STATUSES.slice(0, currentStatusIndex + 1).map((status, index) => ({
        creationDate: formatDateTime(
          faker.date.recent({
            days: Math.max(1, 30 - index * 5),
            refDate: new Date(),
          }),
        ),
        status,
      }));

      return {
        customerId: faker.string.uuid(),
        orderNumber: Number.isNaN(parsedOrderNumber) ? faker.number.int({ min: 100000, max: 999999 }) : parsedOrderNumber,
        status: currentStatus,
        historicStatus,
        items: buildOrderItems(),
      };
    }),
  );
}

export function getPayment(orderNumber: string): Payment {
  return getOrCreate(paymentsCache, orderNumber, () =>
    withSeed(`payment:${orderNumber}`, () => ({
      id: faker.string.uuid(),
      paymentType: faker.helpers.arrayElement(PAYMENT_TYPES),
      totalValue: Number(faker.commerce.price({ min: 10, max: 5000, dec: 2 })),
    })),
  );
}

export function getProduct(sku: string): Product {
  return getOrCreate(productsCache, sku, () =>
    withSeed(`product:${sku}`, () => {
      const parsedSku = Number(sku);

      return {
        sku: Number.isNaN(parsedSku) ? faker.number.int({ min: 1000, max: 999999 }) : parsedSku,
        name: faker.commerce.productName(),
        description: faker.commerce.productDescription(),
        imageUrl: `https://picsum.photos/seed/${sku}/400/400`,
      };
    }),
  );
}
