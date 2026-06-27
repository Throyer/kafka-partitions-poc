export type Customer = {
  id: string;
  nome: string;
  email: string;
};

export type OrderStatus = {
  id: number;
  name: string;
};

export type HistoricStatus = {
  creationDate: string;
  status: OrderStatus;
};

export type Order = {
  orderNumber: number;
  status: OrderStatus;
  historicStatus: HistoricStatus[];
};

export type Payment = {
  id: string;
  paymentType: string;
  totalValue: number;
};
