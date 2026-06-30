import type { OrderStatus } from "./types";

export const ORDER_STATUSES: OrderStatus[] = [
  { id: 175, name: "INTEGRADO" },
  { id: 1011, name: "CRIADO" },
  { id: 102, name: "AGUARDANDO_PAGAMENTO" },
  { id: 103, name: "PAGAMENTO_APROVADO" },
  { id: 109, name: "EM_SEPARACAO" },
  { id: 145, name: "RUPTURA_PARCIAL" },
  { id: 112, name: "SEPARADO" },
  { id: 126, name: "NOTA_FISCAL_EMITIDA" },
  { id: 148, name: "EM_PROCESSO_DE_ENTREGA" },
  { id: 137, name: "ENTREGE" },
  { id: 134, name: "CANCELADO" },
];

export const CANCELADO = ORDER_STATUSES.find((status) => status.name === "CANCELADO")!;
export const HAPPY_PATH = ORDER_STATUSES.filter((status) => status.name !== "CANCELADO");
