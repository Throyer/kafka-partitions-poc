#!/usr/bin/env bun

import { formatDateTime } from "./date-utils";
import { CANCELADO, HAPPY_PATH } from "./order-statuses";
import type { HistoricStatus, Order, OrderItem, OrderStatus } from "./types";

type Config = {
  baseUrl: string;
  mockBaseUrl: string;
  orders: number;
  delayMs: number;
  concurrency: number;
  cancelRate: number;
  orderNumberStart: number;
  jitterMs: number;
};

type Stats = {
  published: number;
  failed: number;
};

function parseArgs(argv: string[]): Config {
  const defaults: Config = {
    baseUrl: process.env.BASE_URL ?? "http://localhost:8080",
    mockBaseUrl: process.env.MOCK_BASE_URL ?? "http://localhost:3001",
    orders: Number(process.env.ORDERS ?? 20),
    delayMs: Number(process.env.DELAY_MS ?? 750),
    concurrency: Number(process.env.CONCURRENCY ?? 5),
    cancelRate: Number(process.env.CANCEL_RATE ?? 0.1),
    orderNumberStart: Number(process.env.ORDER_NUMBER_START ?? Date.now() % 1_000_000_000),
    jitterMs: Number(process.env.JITTER_MS ?? 250),
  };

  const config = { ...defaults };

  for (let index = 0; index < argv.length; index += 1) {
    const arg = argv[index];

    if (arg === "--help" || arg === "-h") {
      printHelp();
      process.exit(0);
    }

    const readValue = (): string => {
      const value = argv[index + 1];
      if (!value || value.startsWith("-")) {
        throw new Error(`valor ausente para ${arg}`);
      }
      index += 1;
      return value;
    };

    switch (arg) {
      case "--base-url":
        config.baseUrl = readValue();
        break;
      case "--mock-base-url":
        config.mockBaseUrl = readValue();
        break;
      case "--orders":
        config.orders = Number(readValue());
        break;
      case "--delay-ms":
        config.delayMs = Number(readValue());
        break;
      case "--concurrency":
        config.concurrency = Number(readValue());
        break;
      case "--cancel-rate":
        config.cancelRate = Number(readValue());
        break;
      case "--order-number-start":
        config.orderNumberStart = Number(readValue());
        break;
      case "--jitter-ms":
        config.jitterMs = Number(readValue());
        break;
      default:
        throw new Error(`argumento desconhecido: ${arg}`);
    }
  }

  validateConfig(config);
  return config;
}

function validateConfig(config: Config) {
  if (!Number.isFinite(config.orders) || config.orders < 1) {
    throw new Error("--orders deve ser >= 1");
  }

  if (!Number.isFinite(config.concurrency) || config.concurrency < 1) {
    throw new Error("--concurrency deve ser >= 1");
  }

  if (!Number.isFinite(config.delayMs) || config.delayMs < 0) {
    throw new Error("--delay-ms deve ser >= 0");
  }

  if (!Number.isFinite(config.cancelRate) || config.cancelRate < 0 || config.cancelRate > 1) {
    throw new Error("--cancel-rate deve estar entre 0 e 1");
  }
}

function printHelp() {
  console.log(`
Simulador de carga de pedidos via POST /triggers/publish/order

Uso:
  bun run load [opções]

Opções:
  --base-url <url>             URL da API (default: http://localhost:8080)
  --mock-base-url <url>        URL do mock (default: http://localhost:3001)
  --orders <n>                 Quantidade de pedidos (default: 20)
  --delay-ms <ms>              Intervalo entre avanços de status (default: 750)
  --concurrency <n>            Pedidos simulados em paralelo (default: 5)
  --cancel-rate <0-1>           Fração de pedidos cancelados (default: 0.1)
  --order-number-start <n>     Número inicial dos pedidos
  --jitter-ms <ms>             Variação aleatória extra entre publicações (default: 250)
  -h, --help                   Exibe esta ajuda

Variáveis de ambiente equivalentes:
  BASE_URL, MOCK_BASE_URL, ORDERS, DELAY_MS, CONCURRENCY, CANCEL_RATE, ORDER_NUMBER_START, JITTER_MS

Exemplo:
  bun run load -- --orders 50 --concurrency 10 --delay-ms 500
`.trim());
}

function sleep(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

function randomInt(min: number, max: number) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function buildItems(seed: number): OrderItem[] {
  const itemCount = (seed % 3) + 1;

  return Array.from({ length: itemCount }, (_, index) => {
    const quantity = ((seed + index) % 5) + 1;
    const unitPrice = Number((((seed + index) % 450) + 10 + Math.random()).toFixed(2));

    return {
      productId: 1000 + seed + index,
      quantity,
      totalItem: Number((unitPrice * quantity).toFixed(2)),
    };
  });
}

function buildHistoricStatus(statuses: OrderStatus[], startedAt: Date): HistoricStatus[] {
  return statuses.map((status, index) => ({
    creationDate: formatDateTime(new Date(startedAt.getTime() + index * 60_000)),
    status,
  }));
}

function buildOrderPayload(
  orderNumber: number,
  customerId: string,
  items: OrderItem[],
  statuses: OrderStatus[],
  startedAt: Date,
): Order {
  const currentStatus = statuses.at(-1)!;

  return {
    customerId,
    orderNumber,
    status: currentStatus,
    historicStatus: buildHistoricStatus(statuses, startedAt),
    items,
  };
}

async function syncOrderToMock(mockBaseUrl: string, payload: Order) {
  const response = await fetch(`${mockBaseUrl}/orders/${payload.orderNumber}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const body = await response.text();
    throw new Error(
      `HTTP ${response.status} ao sincronizar pedido ${payload.orderNumber} no mock: ${body}`,
    );
  }
}

async function publishOrder(baseUrl: string, payload: Order) {
  const response = await fetch(`${baseUrl}/triggers/publish/order`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const body = await response.text();
    throw new Error(`HTTP ${response.status} ao publicar pedido ${payload.orderNumber}: ${body}`);
  }
}

function resolveStatusPath(cancelRate: number): OrderStatus[] {
  if (Math.random() >= cancelRate) {
    return [...HAPPY_PATH];
  }

  const cancelAtIndex = randomInt(2, HAPPY_PATH.length - 1);
  return [...HAPPY_PATH.slice(0, cancelAtIndex), CANCELADO];
}

async function simulateOrder(config: Config, orderNumber: number, stats: Stats) {
  const customerId = crypto.randomUUID();
  const items = buildItems(orderNumber);
  const statusPath = resolveStatusPath(config.cancelRate);
  const historicStatuses: OrderStatus[] = [];
  const startedAt = new Date();

  for (let step = 0; step < statusPath.length; step += 1) {
    const nextStatus = statusPath[step]!;

    if (historicStatuses.some((status) => status.id === nextStatus.id)) {
      throw new Error(
        `pedido ${orderNumber}: status ${nextStatus.id} (${nextStatus.name}) repetido na posição ${step + 1}`,
      );
    }

    historicStatuses.push(nextStatus);
    const payload = buildOrderPayload(orderNumber, customerId, items, historicStatuses, startedAt);

    try {
      await syncOrderToMock(config.mockBaseUrl, payload);
      await publishOrder(config.baseUrl, payload);
      stats.published += 1;
      console.log(
        `[ok] pedido ${orderNumber} -> ${payload.status.id} ${payload.status.name} (${step + 1}/${statusPath.length})`,
      );
    } catch (error) {
      stats.failed += 1;
      console.error(`[erro] pedido ${orderNumber}:`, error instanceof Error ? error.message : error);
      return;
    }

    if (step < statusPath.length - 1) {
      const waitMs = config.delayMs + randomInt(0, config.jitterMs);
      await sleep(waitMs);
    }
  }
}

async function runWithConcurrency<T>(
  items: T[],
  concurrency: number,
  worker: (item: T) => Promise<void>,
) {
  let cursor = 0;

  async function runWorker() {
    while (cursor < items.length) {
      const currentIndex = cursor;
      cursor += 1;
      await worker(items[currentIndex]!);
    }
  }

  const workers = Array.from({ length: Math.min(concurrency, items.length) }, () => runWorker());
  await Promise.all(workers);
}

async function main() {
  const config = parseArgs(process.argv.slice(2));
  const stats: Stats = { published: 0, failed: 0 };
  const orderNumbers = Array.from({ length: config.orders }, (_, index) => config.orderNumberStart + index);
  const startedAt = Date.now();

  console.log("Iniciando simulação de carga");
  console.log(
    JSON.stringify(
      {
        baseUrl: config.baseUrl,
        mockBaseUrl: config.mockBaseUrl,
        orders: config.orders,
        concurrency: config.concurrency,
        delayMs: config.delayMs,
        cancelRate: config.cancelRate,
        orderNumberStart: config.orderNumberStart,
        orderNumberEnd: orderNumbers.at(-1),
      },
      null,
      2,
    ),
  );

  await runWithConcurrency(orderNumbers, config.concurrency, async (orderNumber) => {
    await simulateOrder(config, orderNumber, stats);
  });

  const elapsedSeconds = ((Date.now() - startedAt) / 1000).toFixed(1);

  console.log("");
  console.log("Simulação finalizada");
  console.log(
    JSON.stringify(
      {
        published: stats.published,
        failed: stats.failed,
        elapsedSeconds,
      },
      null,
      2,
    ),
  );

  if (stats.failed > 0) {
    process.exit(1);
  }
}

main().catch((error) => {
  console.error(error instanceof Error ? error.message : error);
  process.exit(1);
});
